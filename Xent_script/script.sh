in_domain_text=all.dev+eval.ara.lmselect.txt
out_domain_text=corpus.all.ara
out_domain_text_parallel=corpus.all.en

echo train LM on the dev/test data
ngram-count -text $in_domain_text -lm in-domain.lm -unk -order 3 -kndiscount -interpolate2 -interpolate3

echo vocabulary
tr ' ' '\n' < $in_domain_text | sort | uniq > in-domain.voc

word_count=`wc -w $in_domain_text | awk '{print $1}'`
echo total word: $word_count

echo shuffle out-domain-data
python script.py $word_count $out_domain_text

echo train LM on the out-domain
ngram-count -text out-domain.subset -lm out-domain.lm -unk -order 3 -kndiscount -interpolate2 -interpolate3 -limit-vocab -vocab in-domain.voc

echo run LM
ngram -lm in-domain.lm -order 3 -unk -ppl $out_domain_text -debug 1 > in-domain.result
ngram -lm out-domain.lm -order 3 -unk -ppl $out_domain_text -debug 1 > out-domain.result

echo parse result
python script2.py $out_domain_text_parallel

echo sort sentences
sort -n output.raw | cut -d' ' -f3- > output.result
sort -n output.parallel.raw | cut -d' ' -f3- > output.parallel.result

echo generate final output
python script3.py `wc -w $out_domain_text | awk '{print $1}'`