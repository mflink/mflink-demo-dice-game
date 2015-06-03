/*
 * Copyright (C) 2013-2014, Infthink (Beijing) Technology Co., Ltd. All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package tv.morefun.demo.dice;

import org.json.JSONException;
import org.json.JSONObject;

import tv.morefun.flint.Flint;
import tv.morefun.flint.FlintDevice;
import tv.morefun.flint.FlintManager;
import tv.morefun.flint.ResultCallback;
import tv.morefun.flint.Status;

import android.util.Log;

public class DiceChannel implements Flint.MessageReceivedCallback {
    private static final String TAG = DiceChannel.class.getSimpleName();

    private static final String GAME_NAMESPACE = "urn:flint:tv.morefun.demo.dice";

    public DiceChannel() {
    }

    public String getNamespace() {
        return GAME_NAMESPACE;
    }

    /**
     * The sender can use that to send String messages to the receiver over that channel
     * @param apiClient
     * @param message
     */
    private final void sendMessage(FlintManager apiClient, String message) {
        Log.d(TAG, "Sending message: (ns=" + GAME_NAMESPACE + ") " + message);
        Flint.FlintApi.sendMessage(apiClient, GAME_NAMESPACE, message)
                .setResultCallback(new SendMessageResultCallback(message));
    }

    @Override
    public void onMessageReceived(FlintDevice flingDevice, String namespace,
            String message) {
        Log.d(TAG, "onTextMessageReceived: " + message);
        JSONObject payload;
        try {
            payload = new JSONObject(message);
            Log.d(TAG, "payload: " + payload);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private final class SendMessageResultCallback implements
            ResultCallback<Status> {
        String mMessage;

        SendMessageResultCallback(String message) {
            mMessage = message;
        }

        @Override
        public void onResult(Status result) {
            if (!result.isSuccess()) {
                Log.d(TAG,
                        "Failed to send message. statusCode: "
                                + result.getStatusCode() + " message: "
                                + mMessage);
            }
        }
    }


    public final void start(FlintManager apiClient) {
        try {
            Log.d(TAG, "start");
            JSONObject payload = new JSONObject();
            payload.put("command", "start");
            sendMessage(apiClient, payload.toString());
        } catch (JSONException e) {
            Log.e(TAG, "Cannot create object to join a game", e);
        }
    }

    public final void stop(FlintManager apiClient) {
        Log.d(TAG, "stop");
        try {
            JSONObject payload = new JSONObject();
            payload.put("command", "stop");
            sendMessage(apiClient, payload.toString());
        } catch (JSONException e) {
            Log.e(TAG, "Cannot create object to send a move", e);
        }
    }
    
}
