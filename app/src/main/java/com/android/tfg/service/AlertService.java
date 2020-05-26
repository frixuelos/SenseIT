package com.android.tfg.service;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.Service;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.IBinder;

import androidx.core.app.NotificationCompat;
import androidx.lifecycle.Observer;
import androidx.preference.PreferenceManager;

import com.android.tfg.R;
import com.android.tfg.model.AlertModel;
import com.android.tfg.model.DeviceModel;
import com.android.tfg.repository.SigfoxRepository;
import com.android.tfg.math.Converter;

import java.util.LinkedList;
import java.util.List;
import java.util.Locale;

public class AlertService extends Service {

    private final String CHANNEL_ID = "ALERTS";
    private int NOTIFICATION_ID;
    private SigfoxRepository sigfoxRepository;
    private NotificationManager notificationManager;
    private NotificationCompat.Builder builder;
    private LinkedList<AlertModel> alerts;
    private LinkedList<DeviceModel> devices;
    private Observer<List<DeviceModel>> obsDev = new Observer<List<DeviceModel>>() {
        @Override
        public void onChanged(List<DeviceModel> deviceModels) {
            // limpiar valores
            devices.clear();
            devices.addAll(deviceModels);
            alerts.clear();

            // Obtener alertas de los dispositivos
            for(DeviceModel device : devices) {
                AlertModel alert = sigfoxRepository.getDeviceAlert(device.getId());
                if(alert!=null){
                    alerts.add(alert);
                    checkAlert(alert, device);
                }
            }
        }
    };

    public AlertService() {
    }

    @Override
    public void onCreate() {
        sigfoxRepository=SigfoxRepository.getInstance(this);
        alerts=new LinkedList<>();
        devices=new LinkedList<>();
        notificationManager=(NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        NOTIFICATION_ID=1;
        super.onCreate();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        sigfoxRepository.getAllDevices().observeForever(obsDev); // Observar los dispositivos
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public IBinder onBind(Intent intent) {
        //  Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }


    /**********************************
     * CHECK IF DEVICE TRIGGERS ALERT *
     * @param alert                   *
     * @param device                  *
     **********************************/
    private void checkAlert(AlertModel alert, DeviceModel device){
        String content = "";
        /********
         * TEMP *
         ********/
        if((alert.isMinTemp() && device.getLastMessage().getTemp()<alert.getMinTempValue())
            || (alert.isMaxTemp() && device.getLastMessage().getTemp()>alert.getMaxTempValue())){
            content+=String.format(Locale.getDefault(), getString(R.string.alertContentFormat), convertTemp(device.getLastMessage().getTemp()), getTempUnits());
        }

        /*********
         *  HUM  *
         *********/
        if((alert.isMinHum() && device.getLastMessage().getHum()<alert.getMinHumValue())
            || (alert.isMaxHum() && device.getLastMessage().getHum()>alert.getMaxHumValue())){
            content+=String.format(Locale.getDefault(), getString(R.string.alertContentFormat), device.getLastMessage().getHum(), getString(R.string.defUnitHum));
        }

        /********
         * PRES *
         ********/
        if((alert.isMinPres() && device.getLastMessage().getPres()<alert.getMinPresValue())
                || (alert.isMaxPres() && device.getLastMessage().getPres()>alert.getMaxPresValue())){
            content+=String.format(Locale.getDefault(), getString(R.string.alertContentFormat), convertPres(device.getLastMessage().getPres()), getPresUnits());
        }

        /******
         * UV *
         ******/
        if((alert.isMinUv() && device.getLastMessage().getUv()<alert.getMinUuValue())
                || (alert.isMaxUv() && device.getLastMessage().getUv()>alert.getMaxUuValue())){
            content+=String.format(Locale.getDefault(), getString(R.string.alertContentFormat), convertUv(device.getLastMessage().getUv()), getUvUnits());
        }

        // Send notification for device
        if(!content.equals("")){
            sendAlert(content);
        }
    }


    private void sendAlert(String content){
        builder = new NotificationCompat.Builder(this, null);

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){ // diferenciar version Oreo o superior (requiere definir un canal de notificaciones)

            // Nombre canal
            CharSequence channelName = getString(R.string.alertChannelName);

            // Descripcion canal y prioridad
            String channelDescription = getString(R.string.alertChannelDescription);
            int priority = NotificationManager.IMPORTANCE_HIGH;

            // Canal
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, channelName, priority);

            // Configuracion canal
            channel.setDescription(channelDescription);
            channel.enableLights(true); // activar led notificacion

            // Colores y vibracion de las notificaciones
            channel.setLightColor(Color.RED);
            channel.enableVibration(true);
            channel.setVibrationPattern(new long[]{100, 200, 300, 400, 500, 400, 300, 200, 400});

            // Crea canal
            notificationManager.createNotificationChannel(channel);

            // Builder
            builder = new NotificationCompat.Builder(this, CHANNEL_ID);

        }

        // Aqui son versiones anteriores a Oreo
        builder.setSmallIcon(R.drawable.ic_device)
                .setContentTitle(getString(R.string.alertNotificationTitle))
                .setContentText(content);

        builder.setChannelId(CHANNEL_ID);

        // Notificar
        notificationManager.notify(NOTIFICATION_ID, builder.build());
        NOTIFICATION_ID+=1;
    }


    /************
     * UNIDADES *
     ************/
    private String getTempUnits(){
        return PreferenceManager.getDefaultSharedPreferences(this).getString(getString(R.string.keyUnitTemp), getString(R.string.defUnitTemp));
    }

    private String getPresUnits(){
        return PreferenceManager.getDefaultSharedPreferences(this).getString(getString(R.string.keyUnitPres), getString(R.string.defUnitPres));
    }

    private String getUvUnits(){
        return PreferenceManager.getDefaultSharedPreferences(this).getString(getString(R.string.keyUnitUV), getString(R.string.defUnitUV));
    }

    private double convertTemp(double temp){
        if(getTempUnits().equals(getString(R.string.KUnitTemp))){
            return Converter.toFarenheit(temp);
        }
        return temp;
    }

    private double convertPres(double pres){
        if(getPresUnits().equals(getString(R.string.ATMUnitPres))){
            return Converter.toATM(pres);
        }
        return pres;
    }

    private double convertUv(double uv){
        if(getUvUnits().equals(getString(R.string.WM2UnitUV))){
            return Converter.toW(uv);
        }
        return uv;
    }

}
