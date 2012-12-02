#include <stdio.h>
#include <sys/types.h>
#include <fcntl.h>
#include <unistd.h>
#include <ext2fs/ext2fs.h>
#include <errno.h>
#include <error.h>
#include <string.h>
#include <sys/time.h>
#include <sys/stat.h>

static const int BG_DESC_SIZE = 32;
static const char* resultDir = "recoveredFiles";
static const int dirNum = EXT2_NDIR_BLOCKS;

static int writeRecoveredFile(int fd, int rFD, struct ext2_inode inode, int* blockbound,
			      char** block_bitmap_array, int blocks_per_group, int mark,
			      int block_group);
static int directWrite(int fd, int rFD, int blockID, int size, int block_size, 
		       char** block_bitmap_array, int blocksNum, 
		       int mark, int block_group);
static int indirectWrite(int fd, int rFD, int blockID, int* blockbound, int index, 
			 int left, char** block_bitmap_array, int blocksNum, 
			 int mark, int block_group);
static int checkBlock(char** block_bitmap_array, int blocksNum, 
		      int blockID, int mark, int block_group);

int main(int argc, char* argv[]) {
  char* filename = argv[1];
  int rcode;
  int fd = open (filename, O_RDONLY);
  if ( fd < 0) error(-1, errno, "read open");

  struct ext2_super_block superblock;
  struct ext2_group_desc desc;

  int block_size;  
  int block_group;
  int inode_size;
  int inodes_number_per;
  int blocks_number_per;
  int blockbound[4];
 
  mkdir( resultDir, S_IRWXU | S_IRWXG | S_IRWXO);
 
  rcode = lseek( fd, SUPERBLOCK_OFFSET, SEEK_SET );
  if (rcode < 0) error(-1, errno, "lseek");
  rcode = read( fd, &superblock, 90 );
  if (rcode < 0) error(-1, errno, "read");
  block_size = 1024 << superblock.s_log_block_size;
  inodes_number_per = superblock.s_inodes_per_group;
  blocks_number_per = superblock.s_blocks_per_group;
  block_group = superblock.s_inodes_count / inodes_number_per;
  inode_size = superblock.s_inode_size;
  if (superblock.s_inodes_count % inodes_number_per) block_group += 1;

  blockbound[0] = block_size;
  blockbound[1] = block_size / 4 * blockbound[0];
  blockbound[2] = block_size / 4 * blockbound[1];
  blockbound[3] = block_size / 4 * blockbound[2];

  char recoveredFile[12 + strlen(resultDir)];
  int rFD;
  struct ext2_inode inode;
  char inode_table[inode_size * inodes_number_per]; 
  char desc_table[BG_DESC_SIZE * block_group];
  char* block_bitmap_array[block_group];

  rcode = lseek(fd, block_size * (superblock.s_first_data_block + 1), SEEK_SET);
  if (rcode < 0) error(-1, errno, "lseek");
  rcode = read( fd, &desc_table, BG_DESC_SIZE * block_group);	
  if (rcode < 0) error(-1, errno, "read");
  
  int i;
  for ( i = 0; i < block_group; i++ ) {
    desc = *(struct ext2_group_desc *)(desc_table + i * BG_DESC_SIZE);

    char* block_bitmap = (char*)malloc(block_size * sizeof(char));
    rcode = lseek( fd, block_size * desc.bg_block_bitmap, SEEK_SET);
    if (rcode < 0) error(-1, errno, "lseek");
    rcode = read( fd, block_bitmap, block_size);
    if (rcode < 0) error(-1, errno, "read");
    block_bitmap_array[i] = block_bitmap;

  }

  for (i = 0; i < block_group; i++ ) {
    desc = *(struct ext2_group_desc *)(desc_table + i * BG_DESC_SIZE);    

    rcode = lseek( fd, block_size * desc.bg_inode_table, SEEK_SET );
    if (rcode < 0) error(-1, errno, "lseek");
    rcode = read( fd, &inode_table, inode_size * inodes_number_per);
    if (rcode < 0) error(-1, errno, "read");
    
    int j;
    int temp;
    int inodesNum = (i == block_group - 1 
		     && (temp = (superblock.s_inodes_count % inodes_number_per)))?
      temp : inodes_number_per;
    for ( j = 0; j < inodesNum ; j++ ) {
      inode = *(struct ext2_inode *)(inode_table + j * inode_size);
      if ((!LINUX_S_ISDIR(inode.i_mode)) && inode.i_dtime > 0) {

	sprintf(recoveredFile, "%s/file-%.5d", resultDir, i * inodes_number_per + j + 1);
	rFD = open(recoveredFile, O_WRONLY | O_CREAT | O_TRUNC, S_IRUSR | S_IWUSR);
	if (rFD < 0) error(-1, errno, "write open");

	int result = writeRecoveredFile(fd, rFD, inode, blockbound, block_bitmap_array, 
					blocks_number_per, superblock.s_first_data_block,
					block_group);

	struct timeval times[2];
	times[0].tv_sec = inode.i_atime;
	times[0].tv_usec = 0;
	times[1].tv_sec = inode.i_mtime;
	times[1].tv_usec = 0;
	if( futimes(rFD, times) < 0 ) error(-1, errno, "futimes");
	if( close(rFD) ) error(-1, errno, "write close");
	if(result) remove(recoveredFile);
      }
    }
  }

  for (i = 0; i < block_group; i++ ){
    free(block_bitmap_array[i]);
  }

  if ( close(fd) ) error(-1, errno, "read close");
  return 0;
}

static int writeRecoveredFile(int fd, int rFD, struct ext2_inode inode, int* blockbound,
			       char** block_bitmap_array, int blocks_number_per, 
			      int mark, int block_group){

  int block_size = blockbound[0];
  int left = inode.i_size;
  int readsize;

  int i;
  for( i = 0; i < dirNum; i++) {
    readsize = (left < block_size)? left : block_size;
    if(directWrite(fd, rFD, inode.i_block[i], readsize, block_size, 
		   block_bitmap_array, blocks_number_per, mark, block_group))
      return 1;
    left -= readsize;
    if (!left) return 0;
  }

  for (i = 1; i < 4; i++) {
    int indirect = blockbound[i];
    readsize = (left < indirect)? left : indirect;
    if(indirectWrite(fd, rFD, inode.i_block[dirNum + i - 1], blockbound, i, readsize, 
		     block_bitmap_array, blocks_number_per, mark, block_group))
      return 1;
    left -= readsize;
    if (!left) return 0;
  }
  return 0;
}


static int directWrite(int fd, int rFD, int blockID, int size, int block_size, 
		       char** block_bitmap_array, int blocksNum, 
		       int mark, int block_group) {
  
  if(checkBlock(block_bitmap_array, blocksNum, blockID, mark, block_group)) return 1;

  char buffer[size];
  int rcode;
  rcode = lseek( fd, block_size * blockID, SEEK_SET);
  if ( rcode < 0 ) error(-1, errno, "lseek");
  rcode = read( fd, buffer, size);
  if ( rcode < 0 ) error(-1, errno, "read");
  rcode = write(rFD, &buffer, size);
  if ( rcode < 0 ) error(-1, errno, "write");

  return 0;
}

static int indirectWrite(int fd, int rFD, int blockID, int* blockbound, int index, 
			 int left, char** block_bitmap_array, int blocksNum, 
			 int mark, int block_group) {
  
  int block_size = blockbound[0];
  int bound = blockbound[index - 1];
  char indirectblock[block_size];

  if(checkBlock(block_bitmap_array, blocksNum, blockID, mark, block_group)) return 1;
  int rcode;
  rcode = lseek( fd, blockID * block_size, SEEK_SET);
  if ( rcode < 0 ) error(-1, errno, "lseek");
  rcode = read( fd, indirectblock, block_size);
  if ( rcode < 0 ) error(-1, errno, "read");

  int num = block_size / 4;
  int i;
  for (i = 0; i < num; i++) {
    int readsize = (left < bound)? left : bound;
    int blockID = *(int *)(indirectblock + 4*i);
    if (index  == 1) {
      if(directWrite(fd, rFD, blockID, readsize, block_size, 
		     block_bitmap_array, blocksNum, mark, block_group)) return 1;
    } else {
      if(indirectWrite( fd, rFD, blockID, blockbound, index - 1, readsize, 
			block_bitmap_array, blocksNum, mark, block_group))
	return 1;
    }
    left -= readsize;
    if (!left) return 0;
  }

  return 0;
}

static int checkBlock(char** block_bitmap_array, int blocksNum, int blockID, 
		      int mark, int block_group) {

  int groupID = (blockID - mark) / blocksNum;
  if (blockID < 0 || groupID >= block_group) return 1;

  int relativeBlock = (blockID - mark) % blocksNum;
  char* block_bitmap = block_bitmap_array[groupID];
  return (block_bitmap[relativeBlock / 8] >> relativeBlock % 8) & 1;

}
    
  

