package founders.blockers.beyondcloudapp;

import androidx.appcompat.app.AppCompatActivity;
import founders.blockers.beyondcloudapp.function.FunctionUtils;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.samsung.android.sdk.blockchain.CoinType;
import com.samsung.android.sdk.blockchain.ListenableFutureTask;
import com.samsung.android.sdk.blockchain.SBlockchain;
import com.samsung.android.sdk.blockchain.account.Account;
import com.samsung.android.sdk.blockchain.account.ethereum.EthereumAccount;
import com.samsung.android.sdk.blockchain.coinservice.CoinNetworkInfo;
import com.samsung.android.sdk.blockchain.coinservice.CoinServiceFactory;
import com.samsung.android.sdk.blockchain.coinservice.ethereum.EthereumService;
import com.samsung.android.sdk.blockchain.exception.SsdkUnsupportedException;
import com.samsung.android.sdk.blockchain.network.EthereumNetworkType;
import com.samsung.android.sdk.blockchain.wallet.HardwareWallet;
import com.samsung.android.sdk.blockchain.wallet.HardwareWalletType;

import org.jetbrains.annotations.NotNull;
import org.web3j.abi.FunctionReturnDecoder;
import org.web3j.abi.TypeReference;
import org.web3j.abi.datatypes.Function;
import org.web3j.abi.datatypes.Type;

import java.math.BigInteger;
import java.util.List;
import java.util.concurrent.ExecutionException;

public class SearchActivity extends AppCompatActivity {

    private SBlockchain sBlockchain;
    private HardwareWallet hardwareWallet;

    Button searchBtn;
    TextView searchResult;

    EthereumService ethereumService;

    String dappSmartcontractAddress;
    String smartContractGetfunctionAddress;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        Intent intent = getIntent();

        sBlockchain = new SBlockchain();

        try{
            sBlockchain.initialize(this);
        }catch(SsdkUnsupportedException e){
            e.printStackTrace();
        }

        searchBtn = findViewById(R.id.search_btn);
        searchResult = findViewById(R.id.searchResultBox);

        searchBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sBlockchain.getHardwareWalletManager()
                        .connect(HardwareWalletType.SAMSUNG, true)
                        .setCallback(new ListenableFutureTask.Callback<HardwareWallet>() {
                            @Override
                            public void onSuccess(HardwareWallet w) {
                                hardwareWallet = w;
                                Log.d("SUCCESS TAG", "connection success");
                                printAll();

                            }

                            @Override
                            public void onFailure(@NotNull ExecutionException e) {

                            }

                            @Override
                            public void onCancelled(@NotNull InterruptedException e) {

                            }
                        });
            }
        });

    }

    /*

    printAll()
    return every transaction data on block chain
    Infura test address : 70ddb1f89ca9421885b6268e847a459d

     */
    private void printAll() {

        String infuraAddress = "70ddb1f89ca9421885b6268e847a459d";
        dappSmartcontractAddress = "0x07d55a62b487d61a0b47c2937016f68e4bcec0e9";
        smartContractGetfunctionAddress = "0x7355a424";

        CoinNetworkInfo coinNetworkInfo = new CoinNetworkInfo(
                CoinType.ETH,
                EthereumNetworkType.ROPSTEN,
                "https://ropsten.infura.io/v3/"+infuraAddress
        );

        List<Account> accountList = sBlockchain.getAccountManager()
                .getAccounts(hardwareWallet.getWalletId(), CoinType.ETH, EthereumNetworkType.ROPSTEN);

        ethereumService = (EthereumService) CoinServiceFactory.getCoinService(this, coinNetworkInfo);


        ethereumService.callSmartContractFunction(
                (EthereumAccount) accountList.get(0),
                dappSmartcontractAddress,
                smartContractGetfunctionAddress
        ).setCallback(new ListenableFutureTask.Callback<String>() {
            @Override
            public void onSuccess(String result) { //return hex string
                Log.d("SUCCESS TAG", "transaction data : "+result);

                getPost(result, accountList);


            }

            @Override
            public void onFailure(@NotNull ExecutionException e) {
                Log.d("ERROR TAG #101",e.toString());
            }

            @Override
            public void onCancelled(@NotNull InterruptedException e) {
                Log.d("ERROR TAG #102",e.toString());
            }
        });


    }


    public void getPost(String result, List<Account> accountList){

        Function functionGetPost = FunctionUtils.countTx();

        List<TypeReference<Type>> outputParameters = functionGetPost.getOutputParameters();

        List<Type> types = FunctionReturnDecoder.decode(result, outputParameters);

        Type type = types.get(0);

        BigInteger post = (BigInteger) type.getValue();

        int length = post.intValue() -1;

        String to = Integer.toString(length);
        Log.d("to : ", to);



        for(int i = 0; i < length; i++){
            String index = Integer.toString(i);

            ethereumService.callSmartContractFunction(
                    (EthereumAccount) accountList.get(0),
                    dappSmartcontractAddress,
                    "0x9507d39a000000000000000000000000000000000000000000000000000000000000000" + index
            ).setCallback(new ListenableFutureTask.Callback<String>() {
                @Override
                public void onSuccess(String s) {
                    Log.d("SUCCESS TAG", "transaction data : "+result);

                    Function functionGetPost = FunctionUtils.callTx(length);

                    List<TypeReference<Type>> outputParameters = functionGetPost.getOutputParameters();

                    List<Type> types = FunctionReturnDecoder.decode(result, outputParameters);

                    Log.d("SUCCESS TAG", "data1 : " + (String) types.get(0).getValue());
                    Log.d("SUCCESS TAG", "data2 : " + (String) types.get(1).getValue());
                    Log.d("SUCCESS TAG", "data3 : " + (String) types.get(2).getValue());

                    searchResult.append("\n" +
                            (String) types.get(0).getValue() +
                            (String) types.get(1).getValue() +
                            (String) types.get(2).getValue());

                }

                @Override
                public void onFailure(@NotNull ExecutionException e) {
                    Log.d("ERROR TAG #201",e.toString());
                }

                @Override
                public void onCancelled(@NotNull InterruptedException e) {
                    Log.d("ERROR TAG #202",e.toString());
                }
            });
        }

    }
}
