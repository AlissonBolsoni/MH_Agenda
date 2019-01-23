package br.com.mindhacks.agenda.utils;

import android.widget.TextView;

import com.github.rtoshiro.util.format.SimpleMaskFormatter;
import com.github.rtoshiro.util.format.text.MaskTextWatcher;

public class MascarasUtils {

    public static void colocaMascara(TextView view, String mascara){
        SimpleMaskFormatter telefomeMask = new SimpleMaskFormatter(mascara);
        MaskTextWatcher telefoneWatcher = new MaskTextWatcher(view, telefomeMask);
        view.addTextChangedListener(telefoneWatcher);
    }

}
