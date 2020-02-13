package founders.blockers.beyondcloudapp;

import org.web3j.abi.TypeReference;
import org.web3j.abi.datatypes.Function;
import org.web3j.abi.datatypes.Uint;
import org.web3j.abi.datatypes.Utf8String;

import java.math.BigInteger;
import java.util.Arrays;
import java.util.Collections;

import okio.Utf8;

import static java.util.Collections.singletonList;

public class FunctionUtils {
    //블록을 생성하는 function과 연결
    public static Function createBLock(String name, String birth, String will) {
        return new Function("set"
                , Arrays.asList(
                new Utf8String(name)
                , new Utf8String(birth)
                , new Utf8String(will))
                , Collections.emptyList());
    }
    //블록에서 데이터를 가지고 오는 함수, int값을 받아서 가지고 온다
    public static Function callBlock(int key) {
        return new Function("get"
                , singletonList(new Uint(BigInteger.valueOf(key)))
                , Arrays.asList(
                new TypeReference<Utf8String>() {
                }
                , new TypeReference<Utf8String>() {
                }
                , new TypeReference<Utf8String>() {
                }


        ));
    }

    //블록 길이(데이터 길이)를 세는 함수
    public static Function countBlock() {
        return new Function("getdatasCount"
                , Collections.emptyList()
                , singletonList(new TypeReference<Uint>() {
        }));

    }
}
