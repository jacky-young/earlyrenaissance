#!/bin/bash
#author: Jacky Young

BIG_TESTS_BASE_DIR="/home/jack/Downloads/node-0.12.7/test/"
CMD_CONFIG="/home/jack/Downloads/node-0.12.7/out/Release/node"
BIG_TESTS_NAME=(message simple pummel internet gc debugger)
TIMEOUT=300

function read_test_children()
{
    local test_children_home=$BIG_TESTS_BASE_DIR$1
    TESTLIST=$(ls $test_children_home | grep .js)
}

function read_out_children()
{
    local test_children_home=$BIG_TESTS_BASE_DIR$1
    OUTLIST=$(ls $test_children_home | grep .out)
}

function get_cmd()
{
    CMD=$CMD_CONFIG
    if [ "$CMD" == "" ]; then
        CMD=$(which node)
        if [ -z "$CMD" ]; then
            echo "please check your node environ!"
            exit 1
        fi
    fi
}

function mkdir_out_bro()
{
    out_bro_path=$BIG_TESTS_BASE_DIR$1"/bro/"
    mkdir -p $out_bro_path
}

function compare_one_line()
{
    local actual=$1
    local expect=$(echo $2 | sed -e 's/*/.*/g' -e 's/\]/\\]/g' -e 's/\[/\\[/g')
    # echo "actual: "$actual
    # echo "expect: "$expect
    if [[ $(echo "$actual" | grep "$expect") != "" ]]; then
        echo "yes"
    else
        echo "no"
    fi
}

function compare_out_file()
{
    local brofile=$1
    local outfile=$2
    sed -i '/^$/d' $brofile
    cp $outfile ${brofile%%.bro}.out
    sed -i '/^$/d' ${brofile%%.bro}.out
    local j=1
    while read outfile_line
    do
        brofile_line=$(sed -n "${j}p" $brofile)
        # echo "brofile_line: "$brofile_line
        # echo "outfile_line: "$outfile_line
        # compare_one_line "${brofile_line}" "${outfile_line}"
        if [[ $(compare_one_line "${brofile_line}" "${outfile_line}") == "yes" ]]; then
            result="match"
        else
            result="nomatch"
            return 1
        fi
        let j=$j+1
    done < "${brofile%%.bro}.out"
    return 0
}

function test_time_out()
{
    local test_cmd=$1
    local test_name=$2
    local seconds=$TIMEOUT
    while [[ $seconds -gt 0 ]]
    do
        # echo "seconds: "$seconds
        sleep 1
        let seconds=$seconds-1
        if [[ $seconds -eq 0 ]]; then
            pid=$(ps -ef | grep "${test_cmd}" | grep "${test_name}" |  tr -s " " | tr " " "\n" | sed -n "2p")
            # echo "pid: "$pid
            if [ "$pid" != "" ]; then
                kill -9 $pid >/dev/null 2>&1 &
            fi
        fi
    done
}

function arr_has_item()
{
    local tmp
    local arr=`echo "$1"`
    for tmp in ${arr[*]}
    do
        if [ "$2" = "$tmp" ]; then
            echo yes
            return
        fi
    done
    echo no
}

if [[ $# -gt 0 ]]; then
    for arg in "$@"
    do
        if [[ $(arr_has_item "`echo ${BIG_TESTS_NAME[*]}`" "$arg") == "no" ]]; then
        # if [[ $(echo "${BIG_TESTS_NAME[@]}" | grep -wq "$arg" && echo "in") != "in" ]]; then
            echo $arg" is not in the list: (${BIG_TESTS_NAME[@]})"
            exit 1
        fi
    done
    RUN_TESTS_NAME=$@
else
    RUN_TESTS_NAME=${BIG_TESTS_NAME[@]}
fi

echo "running list: ${RUN_TESTS_NAME[@]}"

for parent in ${RUN_TESTS_NAME[@]}
do
    echo "[-----------------|"${parent}"|----------------|$(date)|----------------]"
    read_test_children ${parent}
    read_out_children ${parent}
    mkdir_out_bro ${parent}
    i=0
    pass_num=0
    fail_num=0
    # timeout_num=0
    total=`echo "$TESTLIST"|tr " " "\n"|wc -l`
    for child in $TESTLIST
    do
        # echo "child: "$child
        test_full_path=$BIG_TESTS_BASE_DIR$parent"/"$child
        get_cmd
        let i=$i+1
        if [ "${parent}" == "message" ]; then
            $CMD "${test_full_path}" > "${out_bro_path}${child%%.js}.bro" 2>&1
            # echo "out: ${out_bro_path}${child%%.js}.bro"
            compare_out_file "${out_bro_path}${child%%.js}.bro" "${BIG_TESTS_BASE_DIR}/${parent}/${child%%.js}.out"
            if [ $? -eq 0 ] && [ "$result" == "match" ]; then
                echo "["$total"|"$i"|pass]: "${child%%.js}" # "$CMD" "$test_full_path
                let pass_num=$pass_num+1
            else
                echo "["$total"|"$i"|fail]: "${child%%.js}" # "$CMD" "$test_full_path
                let fail_num=$fail_num+1
            fi
        else
            test_time_out "${CMD}" "${test_full_path}" &
            $CMD "${test_full_path}" >/dev/null 2>&1
            if [ $? -eq 0 ]; then
                echo "["$total"|"$i"|pass]: "${child%%.js}" # "$CMD" "$test_full_path
                let pass_num=$pass_num+1
            else
                echo "["$total"|"$i"|fail]: "${child%%.js}" # "$CMD" "$test_full_path
                let fail_num=$fail_num+1
            fi
        fi
    done
    echo "[----------------|"${parent}"|total:"$total"|pass:"$pass_num"|fail:"$fail_num"|$(date)|----------------]"
done

if [ -d "${out_bro_path}" ]; then
    rm -rf ${out_bro_path}
fi