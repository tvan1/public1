package openbd.ivan.probadvuhurovspisok;

import android.Manifest;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ExpandableListView;
import android.widget.Toast;
import android.provider.MediaStore;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;



import org.w3c.dom.Document;

import androidx.activity.ComponentActivity;


public class MainActivity extends AppCompatActivity {

    private static final int REQUEST_CODE_STORAGE_PERMISSION = 1000;
    private ExpandableListView expandableListView;
    private ExpandableListAdapter expandableListAdapter;
    private List<String> groupList;
    private HashMap<String, List<String>> childMap;
    private static final int REQUEST_CODE_STORAGE = 101;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        expandableListView = findViewById(R.id.expandableListView);

        Context context = getApplicationContext();


      //  sms2= readTextFile(context,"sms2level.txt" );
      //  saveToInternalStorage(context,"sms2level.txt",sms2 );
        sms2= readFromAssets(context,"sms2level.txt" );
       // sms2 =   readContentFromString(context,"/sms2level.txt" );
        datasms();
        expandableListAdapter = new ExpandableListAdapter(this, groupListSms, childMapSms);

        expandableListView.setAdapter(expandableListAdapter);

        // Обработчик кликов по элементам
        expandableListView.setOnChildClickListener((parent, v, groupPosition, childPosition, id) -> {
            Toast.makeText(this,
                    "Selected: " + childMapSms.get(groupListSms.get(groupPosition)).get(childPosition),
                    Toast.LENGTH_SHORT).show();
            return false;
        });


        // Проверка и запрос разрешения
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(
                    this,
                    new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                    REQUEST_CODE_STORAGE
            );
        }
    }

   public String sms2;

    private void logshow(String text)
    {
        if (text != null) {
            Log.d("TAG2", text);
        }
    }
    private void logshow(Map<String, String> map)
    {
        map.forEach((key, value) -> System.out.println("Ключ: " + key + ", Значение: " + value));


    }
    private void logshow(List<twoStringsclass> list ) {
        list.forEach(m -> Log.d("Tag 3", "Group:" + m.getGroup() + " Text:" + m.getText()));

    }


    private List<String> groupListSms;
    HashMap<String, List<String>>  childMapSms = new HashMap<>();

    private void datasms_Timur()
    {
        groupListSms = new ArrayList<>();
        Map<String, List<String>> childMapSmsTemp = new HashMap<String, List<String>>();

        String [] map = sms2.split("\n");
        int group = 0;
        for (int i = 0 ; i < map.length; i++) {
            if (map[i].charAt(0) == '#') {
                groupListSms.add(map[i]);
                childMapSmsTemp.put(map[i], new ArrayList<String>());
                group = i;
            } else {
                childMapSmsTemp.get(map[group]).add(map[i]);
            }
        }
        // возвращает hashmap<String, List<String>>
        childMapSms.putAll(childMapSmsTemp);
    }

    private void datasms()
    {
        groupListSms = new ArrayList<>();

        List<twoStringsclass> map3 = new ArrayList<>();
        String [] map = sms2.split("\n");
        Map<String,String> test = new HashMap<>();
       // List<String>group1 = new ArrayList<>();
        int group=-1;
        for (int i=0 ; i<map.length; i++) {
            if (map[i].charAt(0)== '#') { groupListSms.add(map[i]); group++;}
            twoStringsclass temp = new twoStringsclass(groupListSms.get(group), map[i]);
            map3.add(temp);
        }
        // logshow(map3);
        childMapSms = fillchildMap(map3);
        // logshow(test);
    }


    private HashMap<String, List<String>>  fillchildMap(List<twoStringsclass> listmap)
    {
        List<String> temp;
        int q ;
        HashMap<String, List<String>> hashchildMap = new HashMap<>();
        for (int j=0 ; j<groupListSms.size(); j++)
        {   q=0;
            temp = new ArrayList<>();
            for (int i=0 ; i<listmap.size(); i++) {
                if ((listmap.get(i).getGroup()).equals(groupListSms.get(j)) ) {
                    q++;
                    if (q==1)continue;
                    temp.add(listmap.get(i).getText());
                }
            }
            hashchildMap.put(groupListSms.get(j),temp);
           // groupListSms.forEach(m -> Log.d("Tag 3", "groupListSms: " +m));
        }
        return hashchildMap;
    }


    private String readFromAssets(Context context, String name) {
        try {
            // 1. Получаем InputStream
            InputStream inputStream = getAssets().open(name);

            // 2. Читаем в строку
            BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
            StringBuilder stringBuilder = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                stringBuilder.append(line).append("\n");
            }
            reader.close();

            // 3. Используем содержимое
            String fileContent = stringBuilder.toString();

            Log.d("AssetsFile", "Content: " + fileContent);
            writefile(context);
            return fileContent;
        } catch (IOException e) {
            Log.e("AssetsFile", "Error reading file: " + e.getMessage());
        return null;
        }

    }
private void writefile(Context context)
{
    File externalFilesDir = context.getExternalFilesDir(null);

// Создаём файл
    File file = new File(externalFilesDir, "example.txt");

    try (FileWriter writer = new FileWriter(file)) {
        writer.write("Тестовое содержимое");
    } catch (IOException e) {
        e.printStackTrace();
    }

}
    private String readContentFromString(Context context, String name) {
        String path = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).getAbsolutePath();
        Log.d("path", "path: " + path); // Вывод в Logcat
        File file = new File(path, name); // Например: "myfile.txt"
        Uri uri = Uri.fromFile(file);

        StringBuilder stringBuilder = new StringBuilder();
        try (InputStream inputStream = context.getContentResolver().openInputStream(uri);
             BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream))) {

            String line;
            while ((line = reader.readLine()) != null) {
                stringBuilder.append(line).append("\n");
            }
            return stringBuilder.toString();

        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static void saveToInternalStorage(Context context, String filename, String content) {
        try (FileOutputStream fos = context.openFileOutput(filename, Context.MODE_PRIVATE)) {
            fos.write(content.getBytes());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static String readTextFile(Context context, String fileName) {
        File file = new File(context.getFilesDir(), fileName);
        if (file.exists()) {
            StringBuilder text = new StringBuilder();
            try (BufferedReader br = new BufferedReader(new FileReader(file))) {
                String line;
                while ((line = br.readLine()) != null) {
                    text.append(line);
                    text.append('\n');
                }
                return text.toString();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return null;
    }
}