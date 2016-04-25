#!/system/bin/sh
#author: Jacky Young


function read_test_children()
{
    local test_children_home=$BIG_TESTS_BASE_DIR"/"$1
    TESTLIST=$(ls $test_children_home | grep .js)
}

function read_out_children()
{
    local test_children_home=$BIG_TESTS_BASE_DIR"/"$1
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
    out_bro_path=$BIG_TESTS_BASE_DIR"/"$1"/bro/"
    mkdir -p $out_bro_path
}

function compare_one_line()
{
    local actual=$1
    local expect=$(echo $2 | sed -e 's/*/.*/g' -e 's/\]/\\]/g' -e 's/\[/\\[/g')
    # echo "actual: "$actual
    # echo "expect: "$expect
    if [[ $(echo "$actual" | grep "$expect") != "" ]]; then
        echo yes
    else
        echo no
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
            pid=$(ps | grep "${test_cmd}" |  tr -s " " | tr " " "\n" | sed -n "2p")
            # echo "pid: "$pid
            if [ "$pid" != "" ]; then
                kill -9 $pid >/dev/null 2>&1 &
            fi
        fi
    done
}

function get_time_now()
{
    echo $(date) | tr -s " " | tr " " "\n" | sed -n "4p"
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

function get_build_in_tests()
{
    local line_num=0
    testpy=$BIG_TESTS_BASE_DIR"/../tools/test.py"
    startline=$(sed -n '/BUILT_IN_TESTS\ =\ \[/=' $testpy)
    # echo $startline
    if [[ -n $startline ]]; then
        let startline=$startline+1
        line=$(sed -n "${startline}p" $testpy | sed "s/\(.*\)'\(.*\)'\(.*\)/\2/g")
        while [[ $(echo $line | grep "]") == "" ]]; do
            if [[ $line == "addons" ]]; then
                children=$(ls -F $BIG_TESTS_BASE_DIR"/addons/" | grep '/$')
                for child in $children
                do
                    BIG_TESTS_NAME[$line_num]=$line"/"$child
                    let line_num=$line_num+1
                done
            else
                BIG_TESTS_NAME[$line_num]=$line
                let line_num=$line_num+1
            fi
            let startline=$startline+1
            line=$(sed -n "${startline}p" $testpy | sed "s/\(.*\)'\(.*\)'\(.*\)/\2/g")
        done
    fi
}


if [[ $# -eq 0 ]]; then
    echo "Node test directory must be needed!"
    echo -e "Usage: ./noderun.sh [OPTION]...\nThis is an shell script for nodejs tests running on python-unsupported target devices." \
            "You need push nodejs and this script to your device.\nThe options below are not needed for all expect -d option.\n" \
            "  -d,\t\tyour nodejs test directory on device, necessary argument\n" \
            "  -m,\t\toptional node cmd path, default system node path\n" \
            "  -r,\t\toptional running test cases list\n" \
            "  -h,\t\tdisplay this help and exit\nYou can use the script like this:\n" \
            "./noderun.sh -d \"/path/to/node*/test/\" [-m \"/path/to/node\"] [-r \"message gc\"]"
    exit 1
fi

while getopts "d:m:r:h" arg
do
    case $arg in
        d)
            # echo "d's arg: $OPTARG"
            if [[ -z "$OPTARG" ]]; then
                echo "Node test directory must be needed!"
                exit 1
            fi
            BIG_TESTS_BASE_DIR=$OPTARG
            get_build_in_tests
            ;;
        m)
            # echo "m's arg: $OPTARG"
            if [[ -n "$OPTARG" ]]; then
                CMD_CONFIG=$OPTARG
            fi
            ;;
        r)
            # echo "r's arg: $OPTARG"
            i=0
            if [[ -n "$OPTARG" ]]; then
                for arg in $OPTARG
                do
                    if [[ $(arr_has_item "`echo ${BIG_TESTS_NAME[*]}`" "$arg") == "no" ]]; then
                        echo $arg" is not in the list: (${BIG_TESTS_NAME[@]})"
                        exit 1
                    fi
                    RUN_TESTS_NAME[$i]=$arg
                    let i=$i+1
                done
            fi
            ;;
        h)
            echo -e "Usage: ./noderun.sh [OPTION]...\nThis is an shell script for nodejs tests running on python-unsupported target devices." \
                    "You need push nodejs and this script to your device.\nThe options below are not needed for all expect -d option.\n" \
                    "  -d,\t\tyour nodejs test directory on device, necessary argument\n" \
                    "  -m,\t\toptional node cmd path, default system node path\n" \
                    "  -r,\t\toptional running test cases list\n" \
                    "  -h,\t\tdisplay this help and exit\nYou can use the script like this:\n" \
                    "./noderun.sh -d \"/path/to/node*/test/\" [-m \"/path/to/node\"] [-r \"message gc\"]"
            exit 0
            ;;
        ?)
            echo "unknow argument!"
            exit 1
            ;;
    esac
done

if [[ -z "${RUN_TESTS_NAME[@]}" ]]; then
    RUN_TESTS_NAME=${BIG_TESTS_NAME[@]}
fi

echo "Node test running directory: "$BIG_TESTS_BASE_DIR
echo -e "Running list: ${RUN_TESTS_NAME[@]}\n"

TIMEOUT=300
RESULTFILE=$BIG_TESTS_BASE_DIR"/result-nodejs-test-edison.log"

if [ -f "$RESULTFILE" ]; then
    rm -rf "$RESULTFILE"
fi
touch $RESULTFILE

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
        test_full_path=$BIG_TESTS_BASE_DIR"/"$parent"/"$child
        get_cmd
        let i=$i+1
        if [ "${parent}" == "message" ]; then
            $CMD "${test_full_path}" > "${out_bro_path}${child%%.js}.bro" 2>&1
            # echo "out: ${out_bro_path}${child%%.js}.bro"
            compare_out_file "${out_bro_path}${child%%.js}.bro" "${BIG_TESTS_BASE_DIR}/${parent}/${child%%.js}.out"
            if [ $? -eq 0 ] && [ "$result" == "match" ]; then
                echo "["$total"|"$i"|pass]: "${child%%.js}" # "$CMD" "$test_full_path
                echo "$(get_time_now) - noderun.sh - RESULTS - Testcase ${parent}-${child}: PASSED" >> $RESULTFILE
                let pass_num=$pass_num+1
            else
                echo "["$total"|"$i"|fail]: "${child%%.js}" # "$CMD" "$test_full_path
                echo "$(get_time_now) - noderun.sh - RESULTS - Testcase ${parent}-${child}: FAILED" >> $RESULTFILE
                echo "=== release ${child} ===" >> $RESULTFILE
                cat "${out_bro_path}${child%%.js}.bro" >> $RESULTFILE
                echo -e "\n" >> $RESULTFILE
                let fail_num=$fail_num+1
            fi
        else
            test_time_out "${CMD}" "${test_full_path}" &
            $CMD "${test_full_path}" >/dev/null 2>$BIG_TESTS_BASE_DIR"/tmp.log"
            if [ $? -eq 0 ]; then
                echo "["$total"|"$i"|pass]: "${child%%.js}" # "$CMD" "$test_full_path
                echo "$(get_time_now) - noderun.sh - RESULTS - Testcase ${parent}-${child}: PASSED" >> $RESULTFILE
                let pass_num=$pass_num+1
            else
                echo "["$total"|"$i"|fail]: "${child%%.js}" # "$CMD" "$test_full_path
                echo "$(get_time_now) - noderun.sh - RESULTS - Testcase ${parent}-${child}: FAILED" >> $RESULTFILE
                echo "=== release ${child} ===" >> $RESULTFILE
                cat "${BIG_TESTS_BASE_DIR}/tmp.log" >> $RESULTFILE
                echo -e "\n" >> $RESULTFILE
                let fail_num=$fail_num+1
            fi
        fi
    done
    echo "[----------------|"${parent}"|total:"$total"|pass:"$pass_num"|fail:"$fail_num"|$(date)|----------------]"
done

if [ -d "${out_bro_path}" ]; then
    rm -rf ${out_bro_path}
fi

if [ -f "${BIG_TESTS_BASE_DIR}/tmp.log" ]; then
    rm -rf $BIG_TESTS_BASE_DIR"/tmp.log"
fi