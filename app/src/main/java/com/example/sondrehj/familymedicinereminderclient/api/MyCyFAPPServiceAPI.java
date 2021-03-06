package com.example.sondrehj.familymedicinereminderclient.api;

import com.example.sondrehj.familymedicinereminderclient.models.Medication;
import com.example.sondrehj.familymedicinereminderclient.models.Message;
import com.example.sondrehj.familymedicinereminderclient.models.TransportReminder;
import com.example.sondrehj.familymedicinereminderclient.models.User;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.HEAD;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

/**
 * API endpoint definitions
 */
public interface MyCyFAPPServiceAPI {
    /**
     * REMINDER REQUESTS
     **/
    @GET("user/{userID}/reminder")
    Call<List<TransportReminder>> getUserReminderList(@Path("userID") String userID);

    @POST("user/{userID}/reminder")
    Call<TransportReminder> createReminder(@Path("userID") String userID, @Body TransportReminder reminder);

    @PUT("user/{userID}/reminder/{reminderID}")
    Call<TransportReminder> updateReminder(@Path("userID") String userID, @Path("reminderID") String reminderID, @Body TransportReminder reminder);

    @DELETE("user/{userID}/reminder/{reminderID}")
    Call<Boolean> deleteReminder(@Path("userID") String userID, @Path("reminderID") String reminderID);


    /**
     * MEDICATION REQUESTS
     **/
    @GET("user/{userID}/medication")
    Call<List<Medication>>getUserMedicationList(@Path("userID") String userID);

    @POST("user/{userID}/medication")
    Call<Medication> createMedication(@Path("userID") String userID, @Body Medication medication);

    @PUT("user/{userID}/medication/{medicationID}")
    Call<Medication> updateMedication(@Path("userID") String userID, @Path("medicationID") String medicationID, @Body Medication medication);

    @DELETE("user/{userID}/medication/{medicationID}")
    Call<Boolean> deleteMedication(@Path("userID") String userID, @Path("medicationID") String medicationID);


    /**
     * USER REQUESTS
     **/
    @GET("user/{userID}/children")
    Call<User> getChildren(@Path("userID") String userID);

    @POST("user")
    Call<User> createUser(@Header("create_secret") String createSecret, @Body User user);

    @PUT("user/{userID}/token/{token}")
    Call<User> associateToken(@Path("userID") String userID, @Path("token") String token);

    @PUT("user/{userID}/settings/graceperiod/{gracePeriod}")
    Call<User> setGracePeriod(@Path("userID") String userID, @Path("gracePeriod") String gracePeriod);

    @PUT("user/{userID}/settings/receivechange/{bool}")
    Call<User> setReceiveChangeNotification(@Path("userID") String userID, @Path("bool") String bool);

    @GET("user/{userID}/lastSeen")
    Call<User> getLastSeenStatus(@Path("userID") String userID);

    @DELETE("user/{userID}/children")
    Call<User> removeChildrenFromGuardian(@Path("userID") String userID);


    /**
     * LINKING REQUESTS
     */
    @POST("user/{userID}/linking/{withID}")
    Call<Message> sendLinkingRequest(@Path("userID") String userID, @Path("withID") String withID);

    @POST("user/{userID}/linkingresponse/{response}")
    Call<Message> responseToLinkingRequest(@Path("userID") String userID, @Path("response") String response);


    /**
     * POLLING REQUESTS
     */
    @HEAD("api/polling")
    Call<Void> sendPollingRequest();
}
