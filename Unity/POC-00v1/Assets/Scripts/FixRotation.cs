using UnityEngine;

public class FixRotation : MonoBehaviour
{

    private int angle = 0;

    // Start is called before the first frame update
    void Start()
    {
        
    }

    // Update is called once per frame
    void Update()
    {
        transform.eulerAngles = new Vector3(0, 0, angle); 
    }

    public void RotateFingerToAngle(int newAngle)
    {
        Debug.Log(string.Format("FixRotation: New Angle received: {0}", newAngle));
        angle = newAngle;
    }
}
