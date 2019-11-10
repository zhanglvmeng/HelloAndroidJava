package com.example.helloandroid_java;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

import helloworld.GreeterGrpc;
import helloworld.Helloworld;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import io.grpc.stub.StreamObserver;

public class MainActivity extends AppCompatActivity {

    public static final String EXTRA_MESSAGE = "com.example.myfirstapp.MESSAGE";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public static ManagedChannel newChannel(String host, int port) {
        return ManagedChannelBuilder.forAddress(host, port).usePlaintext().build();
    }

    public void sendMessage(View view) {
        // TODO
        System.out.println("send 开始");
        // gRPC 交互
        final GreeterGrpc.GreeterStub greeterStub = GreeterGrpc.newStub(newChannel("192.168.1.9", 50051));
//        final GreeterGrpc.GreeterStub greeterStub = GreeterGrpc.newStub(newChannel("127.0.0.1", 50051));
        System.out.println("构建greeterStub 完成");
        Helloworld.HelloRequest request = Helloworld.HelloRequest.newBuilder().setName("zhangpengAAA").build();
        greeterStub.sayHello(request, new StreamObserver<Helloworld.HelloReply>() {
            @Override
            public void onNext(Helloworld.HelloReply value) {
                Log.i("greeterStub", value.getMessage());
            }

            @Override
            public void onError(Throwable t) {
                Log.e("greeterStubError1", t.getMessage());
                Log.e("greeterStubError2", t.getStackTrace().toString());
            }

            @Override
            public void onCompleted() {

            }
        });

        // 跟第二个页面交互
        Intent intent = new Intent(this, DisplayMessageActivity.class);
        EditText editText = (EditText) findViewById(R.id.editText);
        String message = editText.getText().toString();
        intent.putExtra(EXTRA_MESSAGE, message);
        startActivity(intent);

    }

}
