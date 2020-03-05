package com.example.charades_p2;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import com.opencsv.CSVReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.FileOutputStream;
import java.io.FileInputStream;
import java.util.StringTokenizer;

public class MainActivity extends AppCompatActivity {


    private int count = 0;
    private EditText minputET;
    private String textstr = "no change";
    private String file = "my file";
    private String[] tokens = new String[30];
    private String[] poseids = new String[30];

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button translatebtn = findViewById(R.id.translatebtn);
        minputET = findViewById(R.id.getText);

        translatebtn.setOnClickListener(new View.OnClickListener()
        {
            public void onClick(View v)
            {
                textstr =minputET.getText().toString();
                writeToFile(textstr);
                translate2sign();
            }
        });
    }

    private void translate2sign()
    {
        String text = readFromFile();
        StringTokenizer st1 = new StringTokenizer(text);
        String[] filenames = new String[30];
        int id = 0;
        Intent intent = new Intent(this, Main2Activity.class);

        for (int i = 0; st1.hasMoreTokens(); i++)
        {
            tokens[i] = st1.nextToken();
            poseids[i] = readCsv(tokens[i]);

        }

        while(poseids[id] != null)
        {
            int i = 0;
            if(poseids[id].equals("not found"))
            {
                id++;
            }else {
                filenames[i] = "pose_" + poseids[id];
                Variables.appendFile(filenames[i]);
                i++;
                id++;
                count++;
                Variables.setCount(count);
            }
        }

        startActivity(intent);
    }

    private void writeToFile(String data) {
        try {
            FileOutputStream fout = openFileOutput(file, MODE_PRIVATE);
            fout.write(data.getBytes());
            fout.close();
        } catch (Exception e) {
            Log.e("login activity", "File not found: " + e.toString());
        }


    }

    private String readFromFile() {
        String record = "";
        int c;

        try {
            FileInputStream fin = openFileInput(file);
            while((c = fin.read())!= -1)
            {
                record = record + Character.toString((char)c);
            }

        }
        catch (Exception e) {
            Log.e("login activity", "File not found: " + e.toString());
        }

        return record;
    }

    public String readCsv(String token)
    {
        String record = "";
        String id = "nothing";

        try
        {   InputStreamReader is = new InputStreamReader(getAssets().open("video_metadata.csv"));

            CSVReader reader = new CSVReader(is);
            reader.readNext();
            String[] nextRecord = reader.readNext();

            record = nextRecord[0].replace("+","");
            while (!token.equalsIgnoreCase(record))
            {
                if((nextRecord = reader.readNext()) != null)
                {
                    record = nextRecord[0].replace("+","");
                }else
                {
                    return "not found";
                }

            }
            id = nextRecord[6];
            return id;
        }catch(IOException ex)
        {
            ex.printStackTrace();
        }


        return "not found";
    }

    public int getCount(){
        return count;
    }
}
