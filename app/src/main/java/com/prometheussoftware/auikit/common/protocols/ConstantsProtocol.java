package com.prometheussoftware.auikit.common.protocols;

import android.util.Size;

public interface ConstantsProtocol {

    //spinner
    Size Spinner_Hud_Size();
    Size Small_Spinner_Hud_Size();
    Size Spinner_Frame_Size();
    int Spinner_Corner_Radius();

    //nav bar
    Size Nav_Bar_Icon_Size();
    int Nav_Bar_Shadow_Size();
    int Nav_Bar_Height();

    //data
    String File_Provider_Authority();
    int Max_Transition_Bitmap_Size();

    //regex
    int Max_Regex_Chars();
    String Regex_Password();
    String Regex_Email();
    String Regex_Phone();
}
