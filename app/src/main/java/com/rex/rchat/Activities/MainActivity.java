package com.rex.rchat.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.rex.common.common.app.Activity;
import com.rex.common.net.SocketManager;
import com.rex.rchat.R;
import com.rex.rchat.SocketService;

import butterknife.BindView;
import butterknife.OnClick;
import icepick.State;

public class MainActivity extends Activity {
    @BindView(R.id.testText)
    TextView text;

    @BindView(R.id.testButton)
    Button b;

    @State
    int mCodeData=0;

    @Override
    protected int getContentLayoutId() {
        return R.layout.activity_main;
    }


    @Override
    protected void initBefore() {
        Intent intent = new Intent(this, SocketService.class);
        Log.d("MainActivity","before start service");
        startService(intent);
        Log.d("MainActivity","after start service");

    }

    @Override
    protected void initData(Bundle savedInstanceState) {
        super.initData(savedInstanceState);
        text.setText(mCodeData+"");

    }

    @OnClick(R.id.testButton)
    public void onClick(View view) {
//        mCodeData++;
//        text.setText(mCodeData+"");
        String text = "Activity与Fragment\n" +
                "　　Fragment是Android honeycomb 3.0新增的概念，Fragment名为碎片不过却和Activity十分相似。\n" +
                "\n" +
                "　　Fragment是用来描述一些行为或一部分用户界面在一个Activity中，\n" +
                "\n" +
                "（1）你可以合并多个fragment在一个单独的activity中建立多个UI面板，\n" +
                "\n" +
                "（2）同时重用fragment在多个activity中。\n" +
                "\n" +
                "　　你可以认为fragment作为一个activity中的一节模块 ，fragment有自己的生命周期，接收自己的输入事件，你可以添加或移除从运行中的activity。\n" +
                "\n" +
                "　　从中可以看出：一个fragment必须总是嵌入在一个activity中，同时fragment的生命周期 受 activity而影响。当activity 暂停，那么所有在这个activity的fragments将被destroy释放。\n" +
                "\n" +
                " \n" +
                "\n" +
                "处理Fragment的生命周期\n" +
                "宿主activity的声明周期直接影响到fragment的生命周期。\n" +
                "\n" +
                " Activity的生命周期：\n" +
                "\n" +
                "\n" +
                "\n" +
                "Fragment生命周期：\n" +
                "\n" +
                "\n" +
                "\n" +
                "　　创建一个fragment你必须创建一个Fragment的子类或存在的子类，比如类似下面的代码\n" +
                "\n" +
                "public static class AndroidFragment extends Fragment {\n" +
                "    @Override\n" +
                "    public View onCreateView(LayoutInflater inflater, ViewGroup container,\n" +
                "                             Bundle savedInstanceState) { \n" +
                "               return inflater.inflate(R.layout.android_fragment, container, false);\n" +
                "    }\n" +
                "}\n" +
                "onAttach()\n" +
                "\n" +
                "　　当fragment和activity被关联时调用。\n" +
                "\n" +
                "onCreate()\n" +
                "当fragment创建时被调用，你应该初始化一些实用的组件，比如在fragment暂停或停止时需要恢复的\n" +
                "\n" +
                "onCreateView()\n" +
                "当系统调用fragment在首次绘制用户界面时，如果画一个UI在你的fragment你必须返回一个View当然了你可以返回null代表这个fragment没有UI.\n" +
                "\n" +
                "onActivityCreated()\n" +
                "\n" +
                "　　当activity的onCreate()方法返回时调用。\n" +
                "\n" +
                "　　onResumed()\n" +
                "\n" +
                "　　在running状态下的可见状态。\n" +
                "\n" +
                "　　onPaused()\n" +
                "\n" +
                "　　另一个activity在前景运行，并且享有焦点，但是这个fragment所在的activity仍然可见（前景activity部分遮挡或者是半透明的）。\n" +
                "\n" +
                "　　onStop()\n" +
                "\n" +
                "　　fragment不可见。可能是因为宿主activity处于stopped状态，或者fragment被remove掉，然后加在了back stack中。\n" +
                "\n" +
                "　　一个处于stopped状态的activity还是存活状态的，所有的状态和成员信息会被系统保持。但是，它不再被用户可见，并且如果宿主activity被kill掉，它也会被kill掉。\n" +
                "\n" +
                "onDestroyView()\n" +
                "\n" +
                "　　当fragment的UI被移除的时候调用。\n" +
                "\n" +
                "onDetach()\n" +
                "\n" +
                "　　当fragment和activity去关联时调用。\n" +
                "\n" +
                " \n" +
                "\n" +
                "数据存储和恢复\n" +
                "\n" +
                "　　和Activity类似，可以用Bundle类对象保存fragment的状态，当activity的进程被kill之后，需要重建activity时，可以用于恢复fragment的状态。\n" +
                "\n" +
                "　　存储时利用onSaveInstanceState()回调函数，恢复时是在 onCreate(), onCreateView(), 或者onActivityCreated()里。\n" +
                "\n" +
                " \n" +
                "\n" +
                "两者的区别\n" +
                "\n" +
                "1.fragment显得更加灵活。可以直接在XML文件中添加<fragment/>,Activity则不能。\n" +
                "\n" +
                "eg：\n" +
                "\n" +
                "<fragment\n" +
                "\n" +
                "Android:id=\"@+id/left_fragment\"\n" +
                "\n" +
                "Android:name=\"com.example.fragmenttest.LeftFragment\"\n" +
                "\n" +
                "…\n" +
                "\n" +
                "…\n" +
                "\n" +
                "…/>\n" +
                "\n" +
                "2.可以在一个界面上灵活的替换一部分页面，activity不可以，做不到。\n" +
                "\n" +
                "　　替换的时候注意要将这个fragment放在返回栈上。\n" +
                "\n" +
                "3.fragment和Activity之间的通信：(也就是控件的相互操控)\n" +
                "\n" +
                "　　fragment控制fragment：得到一个Activity，然后通过这个Activity的getFragmentManager()获得该Fragment的实例。\n" +
                "\n" +
                "　　fragment控制Activity：这个很简单。每个Fragment都有getActivity()得到一个Activity的实例：\n" +
                "\n" +
                "View listView = getActivity().findViewById(R.id.list);PS:在当前activity和fragment已经进行关联的情况下否则返回null。\n" +
                "\n" +
                "　　Activity控制fragment：activity也可以获得一个fragment的引用，从而调用fragment中的方法：\n" +
                "\n" +
                "xxxFragment xxx=getFragmentManager().findFragmentById();\n" +
                "\n" +
                "　　Activity控制Activity：这个显然是通过Intent活动之间的通信完成。别忘了在被打开的活动中创建Intent和得到Intent一起进行，写个静态的actionStart()。\n" +
                "\n" +
                "4.fragment和Activity中控件的加载\n" +
                "\n" +
                "　　Fragment的载入是通过OnCreateView的时候通过inflater.inflate()加载布局，然后通过修改main.xml，在main.xml上增加注册fragment标签，然后通过android：name来载入你已经通过inflater加载的隐藏布局。\n" +
                "\n" +
                "　　有几个关键点：fragment是通过inflater加载View然后在main.xml中注册得到的。当然如果你可以在fragment中得到View那就可以通过View.findViewId()来操控fragment上的具体控件。\n" +
                "\n" +
                "5.动态加载不同的fragment：\n" +
                "\n" +
                "　　首先，监听你的按钮。\n" +
                "\n" +
                "1.创建待加载fragment的实例\n" +
                "\n" +
                "2.得到FragmentManager，在actibity中可以直接调用getFragmentManager()方法获得。\n" +
                "\n" +
                "3.调用Manager的BeginTansation()\n" +
                "\n" +
                "4.用replace()改变不同的Fragment\n" +
                "\n" +
                "5.commit事务。\n" +
                "\n" +
                "6.Back Stack\n" +
                "\n" +
                "　　activity和fragment生命周期最重要的不同之处是它们如何存储在各自的back stack中。\n" +
                "\n" +
                "　　Activity停止时，是存在一个由系统维护的back stack中，但是当fragment停止（被remove）时，需要程序员显式地调用addToBackStack() ，并且fragment是存在一个由宿主activity掌管的back stack中。\n" +
                "\n" +
                "创建事件回调\n" +
                "　　些情况下，可能需要fragment和activity共享事件，一个比较好的做法是在fragment里面定义一个回调接口，然后要求宿主activity实现它。\n" +
                "\n" +
                "当activity通过这个接口接收到一个回调，它可以同布局中的其他fragment分享这个信息。\n" +
                "\n" +
                "　　例如，一个新闻显示应用在一个activity中有两个fragment，一个fragment A显示文章题目的列表，一个fragment B显示文章。\n" +
                "\n" +
                "　　所以当一个文章被选择的时候，fragment A必须通知activity，然后activity通知fragment B，让它显示这篇文章。\n" +
                "\n" +
                "　　这个情况下，在fragment A中声明一个这样的接口OnArticleSelectedListener：\n" +
                "\n" +
                "public static class FragmentA extends ListFragment {\n" +
                "...\n" +
                "// Container Activity must implement this interface\n" +
                "　　public interface OnArticleSelectedListener {\n" +
                "　　public void onArticleSelected(Uri articleUri);\n" +
                "　　　　}\n" +
                "　　...\n" +
                "}\n" +
                "\n" +
                "　　之后包含这个fragment的activity实现这个OnArticleSelectedListener接口，用覆写的onArticleSelected()方法将fragment A中发生的事通知fragment B。\n" +
                "\n" +
                "　　为了确保宿主activity实现这个接口，fragment A的onAttach() 方法（这个方法在fragment 被加入到activity中时由系统调用）中通过将传入的activity强制类型转换，实例化一个OnArticleSelectedListener对象：\n" +
                "\n" +
                "public static class FragmentA extends ListFragment {\n" +
                "　　OnArticleSelectedListener mListener;\n" +
                "...\n" +
                "　　@Override\n" +
                "　　public void onAttach(Activity activity) {\n" +
                "　　super.onAttach(activity);\n" +
                "　　　　try {\n" +
                "　　　　mListener = (OnArticleSelectedListener) activity;\n" +
                "　　　　　　} catch (ClassCastException e) {\n" +
                "　　　　　　throw new ClassCastException(activity.toString() + \" must implement 　　　　OnArticleSelectedListener\");\n" +
                "　　　　　　}\n" +
                "　　　　}\n" +
                "　　...\n" +
                "}\n" +
                "\n" +
                "　　如果activity没有实现这个接口，fragment将会抛出ClassCastException异常，如果成功了，mListener将会是activity实现OnArticleSelectedListener接口的一个引用，所以通过调用OnArticleSelectedListener接口的方法，fragment A可以和activity共享事件。\n" +
                "\n" +
                "　　比如，如果fragment A是ListFragment的子类，每一次用户点击一个列表项目，系统调用fragment中的onListItemClick() 方法，在这个方法中可以调用onArticleSelected()方法与activity共享事件。\n" +
                "\n" +
                "public static class FragmentA extends ListFragment {\n" +
                "　　OnArticleSelectedListener mListener;\n" +
                "　　...\n" +
                "　　@Override\n" +
                "　　public void onListItemClick(ListView l, View v, int position, long id) {\n" +
                "　　// Append the clicked item's row ID with the content provider Uri\n" +
                "　　　　Uri noteUri = ContentUris.withAppendedId(ArticleColumns.CONTENT_URI, id);\n" +
                "　　// Send the event and Uri to the host activity\n" +
                "　　　　mListener.onArticleSelected(noteUri);\n" +
                "　　　　　　}\n" +
                "　　　　...\n" +
                "　　}";
//        for(int i = 0; i < 100; i++) {
//            for (int j = 0; j < 100; j++){
//                Log.d("MainActivity", "before append msg: " + String.valueOf(i));
            SocketManager.getInstance().sendMessage("here is the data " + text);
//            Log.d("MainActivity", "after append msg: " + String.valueOf(i));
//            }
//            try {
//                Thread.sleep(5000);
//            } catch (InterruptedException e) {
//                e.printStackTrace();
//            }
        }
    }
