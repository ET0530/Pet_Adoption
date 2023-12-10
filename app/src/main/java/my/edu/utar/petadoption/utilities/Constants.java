package my.edu.utar.petadoption.utilities;


import java.util.HashMap;

public class Constants {
    public static final String KEY_COLLECTION_USERS = "users";
    public static final String KEY_NAME = "name";
    public static final String KEY_EMAIL = "email";
    public static final String KEY_PASSWORD = "password";
    public static final String KEY_PREFERENCE_NAME = "chatFunctionPreference";
    public static final String KEY_IS_SIGNED_IN = "isSigned";
    public static final String KEY_USER_ID = "userId";
    public static final String KEY_IMAGE = "image";
    public static final String KEY_FCM_TOKEN = "fcmToken";
    public static final String KEY_USER = "user";
    public static final String KEY_COLLECTION_CHAT = "chat";
    public static final String KEY_SENDER_ID = "senderId";
    public static final String KEY_RECEIVER_ID = "receiverId";
    public static final String KEY_MESSAGE = "message";
    public static final String KEY_TIMESTAMP = "timestamp";
    public static final String KEY_COLLECTION_CONVERSATIONS = "conversations";
    public static final String KEY_SENDER_NAME = "senderName";
    public static final String KEY_RECEIVER_NAME = "receiverName";
    public static final String KEY_SENDER_IMAGE = "senderImage";
    public static final String KEY_RECEIVER_IMAGE = "receiverImage";
    public static final String KEY_LAST_MESSAGE = "lastMessage";
    public static final String KEY_AVAILABILITY = "availability";
    public static final String REMOTE_MSG_AUTHORIZATION = "Authorization";
    public static final String REMOTE_MSG_CONTENT_TYPE = "Content-Type";
    public static final String REMOTE_MSG_DATA = "data";
    public static final String REMOTE_MSG_REGISTRATION_IDS = "registration_ids";
    public static final String KEY_GENDER = "gender";
    public static final String KEY_CONTACT = "contact";
    public static final String KEY_PMESSAGES = "pmessage";
    public static final String KEY_COLLECTION_POST = "post";
    public static final String KEY_POST_ID = "postId";
    public static final String KEY_POST_NAME = "postName";
    public static final String KEY_POSTER_EMAIL = "posterEmail";
    public static final String KEY_POST_TITLE = "postTitle";
    public static final String KEY_POST_DESCRIPTIONS = "descriptions";
    public static final String KEY_POST_IMAGE = "postImage";

    public static HashMap<String, String> remoteMsgHeaders = null;
    public static HashMap<String, String> getRemoteMsgHeaders(){
        if (remoteMsgHeaders == null){
            remoteMsgHeaders = new HashMap<>();
            remoteMsgHeaders.put(
                    REMOTE_MSG_AUTHORIZATION,
                    "key=AAAAADeXznc:APA91bFsHU7B9jqjKCt5_JcG-aCfijzOem5LLJ6BAGJjSVPHZB100T_t-mL45Xw00vgjaAH9Xl7-yA6RZm-KyYnvqzhlFSuIjGsmAWbLEgKntN1xbcCtyCY_3QYOBTkS6nhbsMXdzmT3"
            );
            remoteMsgHeaders.put(
                    REMOTE_MSG_CONTENT_TYPE,
                    "application/json"
            );
        }
        return remoteMsgHeaders;
    }
}