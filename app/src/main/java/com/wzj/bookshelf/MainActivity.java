package com.wzj.bookshelf;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomSheetBehavior;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import android.widget.Spinner;

import com.github.clans.fab.FloatingActionButton;
import com.github.clans.fab.FloatingActionMenu;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.InvalidClassException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;


public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
//    public static final  String TAG="MainActivity";

    private Toolbar toolbar;
    private FloatingActionMenu add_menu;
    private FloatingActionButton add1,add2;
    private DrawerLayout drawer;
    private NavigationView navigationView;
    private ListView listView;
    private List<Book> book_list= new ArrayList<Book>();
    private SearchView searchview;
    private Spinner spinner;
    private List<String> spinner_list = new ArrayList<String>();

    private BottomSheetBehavior bottomSheetBehavior;
    private TextView bottom_dialog_name;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        setListView();
        setBottomSheet();
        setToolBar();
        setFloatingMenu();
        setDrawerLayout();
        setNavigationView();
        setSortButton(toolbar);
        setSpinner();

    }

    private void setBottomSheet() {
        bottomSheetBehavior = BottomSheetBehavior.from(findViewById(R.id.bottom_dialog));
        bottom_dialog_name = (TextView)findViewById(R.id.bottom_dialog_bookname);
        bottomSheetBehavior.setState(BottomSheetBehavior.STATE_HIDDEN);
    }

    private void setListView()
    {
        listView = (ListView)findViewById(R.id.booklist);

        File file = new File(getFilesDir(),"book.xml");
        XmlSerialize xml = new XmlSerialize();

//        if(!file.exists()){
            xml.write_xml(MainActivity.this,book_list,file);
//        }

//        for(int i =0;i<3;i++)
//        {
//            book_list.get(i).setCover(R.drawable.ic_menu_camera);
//        }

        book_list = xml.read_xml(this,file);

        listView.setAdapter(new ListViewAdapter(this,book_list));
        listView.setOnItemClickListener(new mItemClick());

    }

    class mItemClick implements AdapterView.OnItemClickListener
    {

        @Override
        public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {


            bottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);

            bottomSheetBehavior.setBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {
                @Override
                public void onStateChanged(@NonNull View view, int newState) {
                    if(newState == BottomSheetBehavior.STATE_EXPANDED){
                        bottom_dialog_name.setText(book_list.get(position).getName());
                    }
                    else
                        {
                            bottom_dialog_name.setText(book_list.get(position).getIntroduct());
                    }
                }

                @Override
                public void onSlide(@NonNull View view, float v) {

                }
            });

            Toast.makeText(MainActivity.this,"这一项",Toast.LENGTH_LONG).show();
        }
    }

    private void setToolBar() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {

            getSupportActionBar().setDisplayShowTitleEnabled(false);

        }
    }

    private void setFloatingMenu() {
        add_menu = (FloatingActionMenu) findViewById(R.id.add_menu);

        add1 = (FloatingActionButton) findViewById(R.id.add1);
        add1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        add2 = (FloatingActionButton) findViewById(R.id.add2);
        add2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
    }

    private void setDrawerLayout() {
        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
    }

    private void setNavigationView() {
        navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
    }

    private void setSortButton(Toolbar toolbar)
    {
            toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {
                switch (menuItem.getItemId())
                {
                    case R.id.action_sort:
                        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                        builder.setTitle("排序依据");
                        final String[] select = {"标题","作者","出版社","出版时间"};

                        //设置事件
                        builder.setSingleChoiceItems(select, 0, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                //设置选择后的响应事件
                                Toast.makeText(MainActivity.this, "选择了"+select[which], Toast.LENGTH_SHORT).show();
                            }
                        });

                        //设置确认按钮

                        DialogInterface.OnClickListener click = new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Toast.makeText(MainActivity.this, "确认了", Toast.LENGTH_SHORT).show();
                            }
                        };
                        builder.setPositiveButton("确认",click);

                        //显示浮窗
                        AlertDialog dialog = builder.create();
                        dialog.show();
                        break;
                }
                return false;
            }
        });
    }

    private void setSpinner() {

        spinner = (Spinner) findViewById(R.id.toolbar_spinner);
        //建立列表
        if(spinner_list.isEmpty())
        {
            spinner_list.add("所有");
        }
        //设置adapter
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item,spinner_list);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        //绑定adapter控件
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id)
            {
                //添加显示书架事件（请记得添加事件后取消toast显示）
                Toast.makeText(MainActivity.this,"你选择了"+ spinner_list.get(position) +"书架",Toast.LENGTH_LONG).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
//        int id = item.getItemId();
//
//        //noinspection SimplifiableIfStatement
//        if (id == R.id.action_settings) {
//            return true;
//        }
//        AlertDialog.Builder builder = new AlertDialog.Builder(this);
//        builder.setTitle("排序依据");
//        final String[] select = {"标题","作者","出版社","出版时间"};
//
//        builder.setSingleChoiceItems(select, 0, new DialogInterface.OnClickListener() {
//            @Override
//            public void onClick(DialogInterface dialog, int which) {
//                //设置选择后的响应事件
//                Toast.makeText(MainActivity.this, "选择了"+select[which], Toast.LENGTH_SHORT).show();
//            }
//        });
//        AlertDialog dialog = builder.create();
//        dialog.show();

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_camera) {
            // Handle the camera action
        } else if (id == R.id.nav_gallery) {

        } else if (id == R.id.nav_slideshow) {

        } else if (id == R.id.nav_manage) {

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}


