using UnityEngine;
using UnityEngine.Events;

#if PLATFORM_ANDROID
using UnityEngine.Android;
#endif

[System.Serializable]
public class BLEEvent : UnityEvent<int> { }

public class BLEHelperBridge : MonoBehaviour
{
    public BLEEvent readBLECallback;

    private const string BLE_HELPER_NAME = "com.gravr.gravrblelibrary.ble.BLEHelper";
    private const string UNITY_ACTIVITY_NAME = "com.unity3d.player.UnityPlayer";
    private AndroidJavaClass unityClass;
    private AndroidJavaObject unityActivity;
    private AndroidJavaObject _bleHelper;

    // Start is called before the first frame update
    void Start()
    {
        InitialisePlugin();
    }

    // Update is called once per frame
    void Update()
    {
        
    }

    private void InitialisePlugin()
    {
        Debug.Log("BLEHelperBridge: Starting plugin initialisation.");

#if PLATFORM_ANDROID

        Debug.Log("BLEHelperBridge: Getting Unity Android Activity.");
        unityClass = new AndroidJavaClass(UNITY_ACTIVITY_NAME);
        unityActivity = unityClass.GetStatic<AndroidJavaObject>("currentActivity");

        var pluginClass = new AndroidJavaClass(BLE_HELPER_NAME);
        Debug.Log("BLEHelperBridge: BLEHelper class received.");

        if (unityActivity != null)
        {
            _bleHelper = pluginClass.CallStatic<AndroidJavaObject>("getHelper", unityActivity);
            InitHelper();
        }

#endif

        Debug.Log("BLEHelperBridge: Finishing plugin initialisation.");
    }

    private void InitHelper()
    {
#if PLATFORM_ANDROID

        if (_bleHelper != null)
        {
            Debug.Log("BLEHelperBridge: Calling initHelper().");
            _bleHelper.Call("initHelper");
        }

#endif
    }

    public void ReadBLE()
    {
        Debug.Log("BLEHelperBridge: ReadBLE() called.");
        var bleValue = _bleHelper.Call<int>("readBLE");
        readBLECallback.Invoke(bleValue);
        Debug.Log(string.Format("BLEHelperBridge: Value {0} read from BLE.", bleValue));
    }
}
