package model.proteinmodel;

import javafx.scene.paint.Color;

import java.util.HashMap;
import java.util.Map;

public class AminoAcidProperties {

    public static final Map<String, Character> threeToOneLetter;
    public static HashMap<Character, Color> aminoAcidColors;

    static {
        aminoAcidColors = new HashMap<>();
        aminoAcidColors.put('C', Color.web("E6E600"));
        aminoAcidColors.put('D', Color.web("E60A0A"));
        aminoAcidColors.put('S', Color.web("FA9600"));
        aminoAcidColors.put('Q', Color.web("00DCDC"));
        aminoAcidColors.put('K', Color.web("145AFF"));
        aminoAcidColors.put('I', Color.web("0F820F"));
        aminoAcidColors.put('P', Color.web("DC9682"));
        aminoAcidColors.put('T', Color.web("FA9600"));
        aminoAcidColors.put('F', Color.web("3232AA"));
        aminoAcidColors.put('N', Color.web("00DCDC"));
        aminoAcidColors.put('G', Color.web("EBEBEB"));
        aminoAcidColors.put('H', Color.web("8282D2"));
        aminoAcidColors.put('L', Color.web("0F820F"));
        aminoAcidColors.put('R', Color.web("145AFF"));
        aminoAcidColors.put('W', Color.web("B45AB4"));
        aminoAcidColors.put('A', Color.web("C8C8C8"));
        aminoAcidColors.put('V', Color.web("0F820F"));
        aminoAcidColors.put('E', Color.web("E60A0A"));
        aminoAcidColors.put('Y', Color.web("3232AA"));
        aminoAcidColors.put('M', Color.web("E6E600"));
    }

    static {
        threeToOneLetter = new HashMap<>();
        threeToOneLetter.put("CYS", 'C');
        threeToOneLetter.put("ASP", 'D');
        threeToOneLetter.put("SER", 'S');
        threeToOneLetter.put("GLN", 'Q');
        threeToOneLetter.put("LYS", 'K');
        threeToOneLetter.put("ILE", 'I');
        threeToOneLetter.put("PRO", 'P');
        threeToOneLetter.put("THR", 'T');
        threeToOneLetter.put("PHE", 'F');
        threeToOneLetter.put("ASN", 'N');
        threeToOneLetter.put("GLY", 'G');
        threeToOneLetter.put("HIS", 'H');
        threeToOneLetter.put("LEU", 'L');
        threeToOneLetter.put("ARG", 'R');
        threeToOneLetter.put("TRP", 'W');
        threeToOneLetter.put("ALA", 'A');
        threeToOneLetter.put("VAL", 'V');
        threeToOneLetter.put("GLU", 'E');
        threeToOneLetter.put("TYR", 'Y');
        threeToOneLetter.put("MET", 'M');
    }
}
