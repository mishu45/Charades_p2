package com.example.charades_p2;

import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;
import android.widget.VideoView;
import androidx.appcompat.app.AppCompatActivity;
import com.arthenica.mobileffmpeg.Config;
import com.arthenica.mobileffmpeg.FFmpeg;
import java.io.FileWriter;
import java.util.ArrayList;

import static com.arthenica.mobileffmpeg.Config.RETURN_CODE_CANCEL;
import static com.arthenica.mobileffmpeg.Config.RETURN_CODE_SUCCESS;


public class Main2Activity extends AppCompatActivity {

    int count = Variables.getCount();
    private VideoView videoView;
    private String[] files = new String[30];
    private String[]  fileline = new String[count];
    private ArrayList<String> videoPath = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);

        videoView = findViewById(R.id.videoView);

        ArrayList<String> filenames = Variables.getFilename();

        for(int i = 0; i < filenames.size(); i++) {
            files[i] = filenames.get(i);
        }

        for(int id = 0 ; id < count; id++) {
            videoPath.add("/storage/emulated/0/DCIM/" + files[id] + ".mp4");
            fileline[id] = "file "+videoPath.get(id);
        }


        writeToFile(fileline);
        String mergepath = "/storage/emulated/0/DCIM/merged.mp4";

        int rc = FFmpeg.execute("-f concat -safe 0 -y -i /storage/emulated/0/DCIM/join.txt -c copy -filter:v "+mergepath);
        if (rc == RETURN_CODE_SUCCESS) {
            Log.i(Config.TAG, "Command execution completed successfully.");
        } else if (rc == RETURN_CODE_CANCEL) {
            Log.i(Config.TAG, "Command execution cancelled by user.");
        } else {
            Log.i(Config.TAG, String.format("Command execution failed with rc=%d and the output below.", rc));
            Config.printLastCommandOutput(Log.INFO);
        }

        videoView.setVideoPath(mergepath);
        videoView.start();
    }

    private void writeToFile(String[] data) {
        try {
            FileWriter fw = new FileWriter("/storage/emulated/0/DCIM/join.txt");
            for(int i = 0; i< data.length; i++)
            {
                fw.write(data[i]);
                fw.write("\n");
            }
            fw.flush();
            fw.close();
        } catch (Exception e) {
            Toast.makeText(getBaseContext(),"file not found" , Toast.LENGTH_LONG).show();
        }
    }
}