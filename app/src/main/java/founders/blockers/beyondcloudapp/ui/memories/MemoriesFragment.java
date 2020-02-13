package founders.blockers.beyondcloudapp.ui.memories;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
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

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import founders.blockers.beyondcloudapp.R;
import founders.blockers.beyondcloudapp.function.FunctionUtils;

public class MemoriesFragment extends Fragment {

    Button searchBtn;
    TextView _name;
    TextView _birth;
    TextView _will;
    ImageView image;

    private SBlockchain sBlockchain;
    private HardwareWallet hardwareWallet;

    private String to;

    private MemoriesViewModel memoriesViewModel;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        memoriesViewModel =
                ViewModelProviders.of(this).get(MemoriesViewModel.class);
        View root = inflater.inflate(R.layout.fragment_memories, container, false);


        sBlockchain = new SBlockchain();
        image=root.findViewById(R.id.imageInputData);

        try {
            sBlockchain.initialize(getContext());
        } catch (SsdkUnsupportedException e) {
            e.printStackTrace();
        }

        _name=root.findViewById(R.id.name_field);
        _birth=root.findViewById(R.id.birth_field);
        _will=root.findViewById(R.id.will_field);
        searchBtn=root.findViewById(R.id.search);
        searchBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                printOne();
            }
        });


        return root;
    }


    private void printOne(){

        sBlockchain.getHardwareWalletManager()
                .connect(HardwareWalletType.SAMSUNG, true)
                .setCallback(new ListenableFutureTask.Callback<HardwareWallet>() {
                    @Override
                    public void onSuccess(HardwareWallet w) {
                        hardwareWallet = w;
                        Log.d("SUCCESS TAG", "connection success" );

                        CoinNetworkInfo coinNetworkInfo;
                        coinNetworkInfo = new CoinNetworkInfo(
                                CoinType.ETH,
                                EthereumNetworkType.ROPSTEN,
                                "https://ropsten.infura.io/v3/70ddb1f89ca9421885b6268e847a459d"
                        );

                         List<Account> accountList = sBlockchain.getAccountManager()
                                .getAccounts(hardwareWallet.getWalletId(),
                                        CoinType.ETH,
                                        EthereumNetworkType.ROPSTEN);

                        EthereumService ethereumService = (EthereumService) CoinServiceFactory.getCoinService(getContext(), coinNetworkInfo);

                        ethereumService.callSmartContractFunction(
                                (EthereumAccount) accountList.get(0),
                                "0x07d55a62b487d61a0b47c2937016f68e4bcec0e9",
                                "0x7355a424"
                        ).setCallback(new ListenableFutureTask.Callback<String>() {
                            @Override
                            public void onSuccess(String s) {
                                Log.d("SUCCESS TAG", "account link success : "+s );

                                Function functionGetPost = FunctionUtils.countTx();

                                List<TypeReference<Type>> outputParameters = functionGetPost.getOutputParameters();

                                List<Type> types = FunctionReturnDecoder.decode(s, outputParameters);

                                Type type = types.get(0);

                                BigInteger post = (BigInteger) type.getValue();

                                int length=post.intValue()-1;

                                if (length < 10){

                                    to = Integer.toString(length);
                                    to="0"+to;

                                }else{

                                    to = Integer.toString(length);
                                }

                                ethereumService.callSmartContractFunction(
                                        (EthereumAccount) accountList.get(0),
                                        "0x07d55a62b487d61a0b47c2937016f68e4bcec0e9",
                                        "0x9507d39a000000000000000000000000000000000000000000000000000000000000000a"
                                ).setCallback(new ListenableFutureTask.Callback<String>() {
                                    @Override
                                    public void onSuccess(String s) {
                                        Log.d("SUCCESS TAG", "get data : "+s );

                                        Function functionGetPost = FunctionUtils.callTx(length);

                                        List<TypeReference<Type>> outputParameters = functionGetPost.getOutputParameters();

                                        List<Type> types = FunctionReturnDecoder.decode(s, outputParameters);

                                        Log.d("SUCCESS TAG", (String) types.get(0).getValue());
                                        Log.d("SUCCESS TAG", (String) types.get(1).getValue());
                                        Log.d("SUCCESS TAG", (String) types.get(2).getValue());

                                        _name.setText((String)types.get(0).getValue());
                                        _birth.setText((String)types.get(1).getValue());
                                        _will.setText((String)types.get(2).getValue());

                                    }

                                    @Override
                                    public void onFailure(@NotNull ExecutionException e) {
                                        Log.d("ERROR TAG #201", e.toString());
                                    }

                                    @Override
                                    public void onCancelled(@NotNull InterruptedException e) {
                                        Log.d("ERROR TAG #202", e.toString());
                                    }
                                });

                            }

                            @Override
                            public void onFailure(@NotNull ExecutionException e) {
                                Log.d("ERROR TAG #101", e.toString());
                            }

                            @Override
                            public void onCancelled(@NotNull InterruptedException e) {
                                Log.d("ERROR TAG #102", e.toString());
                            }
                        });

                    }

                    @Override
                    public void onFailure(@NotNull ExecutionException e) {
                        Log.d("ERROR TAG #301", e.toString());
                    }

                    @Override
                    public void onCancelled(@NotNull InterruptedException e) {
                        Log.d("ERROR TAG #302", e.toString());
                    }
                });
    }
}