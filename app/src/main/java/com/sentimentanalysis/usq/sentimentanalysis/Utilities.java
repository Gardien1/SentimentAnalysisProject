package com.sentimentanalysis.usq.sentimentanalysis;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;

public abstract class Utilities {

    public static void saveObject(Serializable object , Context context , String fileName)
    {

        File filePath = new File(context.getFilesDir(),fileName);

        try
        {
            if(!filePath.exists())
            {
                filePath.createNewFile();
            }

            ObjectOutputStream output =
                    new ObjectOutputStream(new FileOutputStream( filePath));


            output.writeObject(object);

            output.close();

        }
        catch(Exception e)
        {

        }




    }


    public static Object loadObject(Context context , String fileName)
    {

        File filePath = new File( context.getFilesDir(),"scan_history.txt" );

        Object loadedobj;

        try
        {
            ObjectInputStream input =
                    new ObjectInputStream(new FileInputStream(filePath));


            loadedobj = input.readObject();


            input.close();

            return loadedobj;

        }
        catch(Exception e)
        {

        }

        return null;

    }

}
