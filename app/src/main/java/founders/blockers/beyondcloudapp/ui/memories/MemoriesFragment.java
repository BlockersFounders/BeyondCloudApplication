package founders.blockers.beyondcloudapp.ui.memories;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;

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

import org.web3j.abi.FunctionReturnDecoder;
import org.web3j.abi.TypeReference;
import org.web3j.abi.datatypes.Function;
import org.web3j.abi.datatypes.Type;

import java.math.BigInteger;
import java.util.List;
import java.util.concurrent.ExecutionException;

import founders.blockers.beyondcloudapp.FunctionUtils;
import founders.blockers.beyondcloudapp.R;

public class MemoriesFragment extends Fragment {

    private SBlockchain sBlockchain;
    private HardwareWallet wallet;
    private Account generatedAccount;
    private Object CoinNetworkInfo;
    private MemoriesViewModel memoriesViewModel;
    Button searchBtn;
    TextView _name;
    TextView _birth;
    TextView _will;
    ImageView image;
    private String to;
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        memoriesViewModel =
                ViewModelProviders.of(this).get(MemoriesViewModel.class);
        View root = inflater.inflate(R.layout.fragment_memories, container, false);
        sBlockchain = new SBlockchain();
        image=root.findViewById(R.id.imageView3);
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
                print();
            }
        });

        return root;
    }
    private void print(){
        sBlockchain.getHardwareWalletManager()
                .connect(HardwareWalletType.SAMSUNG,true)
                .setCallback(new ListenableFutureTask.Callback<HardwareWallet>() {
                    @Override
                    public void onSuccess(HardwareWallet hardwareWallet) {
                        wallet = hardwareWallet;
                        Log.d("hi","hi");
                        com.samsung.android.sdk.blockchain.coinservice.CoinNetworkInfo coinNetworkInfo = new CoinNetworkInfo(
                                CoinType.ETH,
                                EthereumNetworkType.ROPSTEN,
                                "https://ropsten.infura.io/v3/70ddb1f89ca9421885b6268e847a459d"
                        );
                        List<Account> accounts=sBlockchain.getAccountManager().getAccounts(wallet.getWalletId(),CoinType.ETH,EthereumNetworkType.ROPSTEN);
                        EthereumService ethereumService= (EthereumService) CoinServiceFactory.getCoinService(getContext(),coinNetworkInfo);

//count 부분
                        ethereumService
                                .callSmartContractFunction(
                                        (EthereumAccount) accounts.get(0),
                                        "0x07d55a62b487d61a0b47c2937016f68e4bcec0e9",
                                        "0x7355a424"
                                        //이 값은 블록에 저장된 데이터의 개수를 불러오는 함수의 encoding된 값이다.
                                )
                                .setCallback(new ListenableFutureTask.Callback<String>() {
                                    @Override
                                    public void onSuccess(String result) {
                                        //result는 hex형식의 string 값이다

                                        Log.d("tet",result);
                                        Function functionGetPost = FunctionUtils.countBlock();
                                        List<TypeReference<Type>> outputParameters = functionGetPost.getOutputParameters();
                                        List<Type> types = FunctionReturnDecoder.decode(result, outputParameters);
                                        Type type = types.get(0);
                                        BigInteger post = (BigInteger) type.getValue();
                                        int length=post.intValue()-1;
                                        if (length<10){
                                            to = Integer.toString(length);
                                            to="0"+to;
                                        }else{
                                            to = Integer.toString(length);
                                        }
                                        //-1하는 이유는 데이터 array는 0부터 시작하기 때문이다. 마지막 값을 불러오려면 data[length-1]하는 원리랑 같다


                                        //여기서 callsmartcontractfunction을 한번 더 불러준다. 위에서 길이를 받아와서 그 길이를 input (int) 값으로 넣어준다.
                                        ethereumService

                                                .callSmartContractFunction(
                                                        (EthereumAccount) accounts.get(0),
                                                        "0x07d55a62b487d61a0b47c2937016f68e4bcec0e9",
                                                        "0x9507d39a000000000000000000000000000000000000000000000000000000000000000a"
                                                        //to값은 위에 길이를 string으로 변환한 것이다. 우선은 블록에 10개 이상이 저장되어있지는 않기 때문에 임의로 한자리라고 가정하고 했다. 자리수가 늘어나면 0을 하나 없애면 된다
                                                )
                                                .setCallback(new ListenableFutureTask.Callback<String>() {
                                                    @Override
                                                    public void onSuccess(String result) {
                                                        Log.d("hello",result);
                                                        Function functionGetPost = FunctionUtils.callBlock(length);
                                                        List<TypeReference<Type>> outputParameters = functionGetPost.getOutputParameters();
                                                        List<Type> types = FunctionReturnDecoder.decode(result, outputParameters);
                                                        Log.d("hi",(String)types.get(0).getValue());
                                                        Log.d("hi",(String)types.get(1).getValue());
                                                        Log.d("hi",(String)types.get(2).getValue());
                                                        //확인차 log값
                                                        _name.setText((String)types.get(0).getValue());
                                                        _birth.setText((String)types.get(1).getValue());
                                                        _will.setText((String)types.get(2).getValue());
                                                        //textview의 값을 바꾸는 부분.여기서 get(0)은 이름 get(1)은 생몰 get(2)는 유언으로 파싱해서 가져올 수 있다. 일단은 통째로 붙여서 넣었다.

                                                        //success
                                                    }
                                                    @Override
                                                    public void onFailure(ExecutionException exception) {
                                                        //failure
                                                    }
                                                    @Override
                                                    public void onCancelled(InterruptedException exception) {
                                                        //cancelled
                                                    }
                                                });

                                        //success
                                    }
                                    @Override
                                    public void onFailure(ExecutionException exception) {
                                        //failure
                                    }
                                    @Override
                                    public void onCancelled(InterruptedException exception) {
                                        //cancelled
                                    }
                                });
                        //호출부분
                    }

                    @Override
                    public void onFailure(ExecutionException e) {

                    }

                    @Override
                    public void onCancelled(InterruptedException e) {

                    }
                });



    }
}