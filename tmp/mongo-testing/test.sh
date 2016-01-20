
TEST_TYPE=$1
N_TESTS=$2

for i in $(seq 1 $N_TESTS); do

    echo Test: $i

    result=`mongo localhost/perf_test $TEST_TYPE | sed '/^[^!]/d' | sed '/^$/d' | sed 's/^\(.* \([0-9]\+\)\)$/\2/'`

    sum=$((sum+result))

    echo Result $i: $result
    # echo $sum
done

avg=$((sum/N_TESTS))

echo average: $avg
