package io;

import model.proteinmodel.AminoAcid;
import model.proteinmodel.AminoAcidProperties;
import model.proteinmodel.Atom;
import model.proteinmodel.Protein;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;


public class PDBParser {

    private static final Set<String> ignored_atoms = new HashSet<>(Arrays.asList("H"));

    public static void parseFile(BufferedReader br, Protein protein) throws IOException {
        AminoAcid currentAa = new AminoAcid(-1);
        StringBuilder sbSequence = new StringBuilder("");
        StringBuilder sbStructure = new StringBuilder("");
        ArrayList<Integer> helixPositions = new ArrayList<>();
        ArrayList<Integer> sheetPositions = new ArrayList<>();
        String line = "";
        int id = 0;
        int internal_id = 1;
        protein.setTitle("");
        while ((line = br.readLine()) != null) {
            String[] columns = splitPdbAtomLine(line);
            if (columns[0].equals("HEADER")) {
                protein.setId(getIdFromHeaderLine(line));
            }
            if (columns[0].equals("TITLE")) {
                protein.setTitle(protein.titleProperty().getValue() + line.substring(6, 54).trim());
            }
            if (columns[0].equals("HELIX")) {
                String[] helixColumns = splitPdbHelixLine(line);
                for (int i = Integer.parseInt(helixColumns[0]); i < Integer.parseInt(helixColumns[1]); i++) {
                    helixPositions.add(i - 1);
                }
            }
            if (columns[0].equals("SHEET")) {
                String[] sheetColumns = splitPdbSheetLine(line);
                for (int i = Integer.parseInt(sheetColumns[0]); i < Integer.parseInt(sheetColumns[1]); i++) {
                    sheetPositions.add(i);
                }
            }
            if (columns[0].equals("ATOM") && AminoAcidProperties.threeToOneLetter.containsKey(columns[3])) {
                if (ignored_atoms.contains(columns[2])) {
                    // ignore atoms
                } else {
                    if (Integer.parseInt(columns[5]) != id) {
                        if (id != 0) {
                            if (currentAa.getAtoms().containsKey("C") && currentAa.getAtoms().containsKey("N") && currentAa.getAtoms().containsKey("O") && currentAa.getAtoms().containsKey("CA")) {
                                protein.getAminoAcids().add(currentAa);
                                internal_id = internal_id + 1;
                            }
                        }
                        id = Integer.parseInt(columns[5]);
                        currentAa = new AminoAcid(internal_id);
                        currentAa.setChainId(columns[4]);
                        currentAa.setLetter(AminoAcidProperties.threeToOneLetter.get(columns[3]));
                        if (AminoAcidProperties.threeToOneLetter.containsKey(columns[3])) {
                            sbSequence.append(AminoAcidProperties.threeToOneLetter.get(columns[3]));
                        }
                        if (protein.getNumberOfAminoAcids().containsKey(AminoAcidProperties.threeToOneLetter.get(columns[3]))) {
                            protein.getNumberOfAminoAcids().put(AminoAcidProperties.threeToOneLetter.get(columns[3]), protein.getNumberOfAminoAcids().get(AminoAcidProperties.threeToOneLetter.get(columns[3])) + 1);
                        } else {
                            protein.getNumberOfAminoAcids().put(AminoAcidProperties.threeToOneLetter.get(columns[3]), 1);
                        }
                        if (helixPositions.contains(internal_id-1)) {
                            currentAa.setStructure('H');
                        } else if (sheetPositions.contains(internal_id-1)) {
                            currentAa.setStructure('E');
                        } else {
                            currentAa.setStructure('-');
                        }
                        Atom atom = parseAtom(columns, currentAa, internal_id);
                        currentAa.getAtoms().put(columns[2], atom);
                        protein.getAtoms().add(atom);
                    } else {
                        id = Integer.parseInt(columns[5]);
                        Atom atom = parseAtom(columns, currentAa, internal_id);
                        currentAa.getAtoms().put(columns[2], atom);
                        protein.getAtoms().add(atom);
                    }
                }
            }
        }

        // make sure atom is complete otherwise ignore... for example in 2JEF not the case!
        if (currentAa.getAtoms().containsKey("C") && currentAa.getAtoms().containsKey("N") && currentAa.getAtoms().containsKey("O") && currentAa.getAtoms().containsKey("CA")) {
            protein.getAminoAcids().add(currentAa);
        }
        protein.setSequence(sbSequence.toString());
        for (int i = 0; i < sbSequence.length(); i++) {
            if (helixPositions.contains(i)) {
                sbStructure.append("H");
            } else if (sheetPositions.contains(i)) {
                sbStructure.append("E");
            } else {
                sbStructure.append("-");
            }
        }
        protein.setStructure(sbStructure.toString());

    }

    private static Atom parseAtom(String[] columns, AminoAcid currentAa, int internal_id) {
        Atom atom = new Atom();
        atom.xProperty().setValue(Double.parseDouble(columns[6]) * 25);
        atom.yProperty().setValue(Double.parseDouble(columns[7]) * 25);
        atom.zProperty().setValue(Double.parseDouble(columns[8]) * 25);
        atom.setAaId(internal_id);
        atom.setChainId(columns[4]);
        atom.setAa(currentAa);
        atom.setAtomId(Integer.parseInt(columns[1]));
        atom.setAtom(columns[2]);
        atom.setType(columns[9].charAt(0));

        return atom;
    }

    private static String[] splitPdbAtomLine(String line) {
        return new String[]{line.substring(0, 6).trim(),
                line.substring(6, 11).trim(),
                line.substring(12, 16).trim(),
                line.substring(17, 20).trim(),
                line.substring(21, 22).trim(),
                line.substring(22, 26).trim(),
                line.substring(30, 38).trim(),
                line.substring(38, 46).trim(),
                line.substring(46, 54).trim(),
                line.substring(77, 78).trim()};
    }

    private static String[] splitPdbHelixLine(String line) {
        return new String[]{line.substring(22, 25).trim(),
                line.substring(34, 37).trim()};
    }

    private static String[] splitPdbSheetLine(String line) {
        return new String[]{line.substring(23, 26).trim(),
                line.substring(34, 37).trim()};
    }

    private static String getIdFromHeaderLine(String line) {
        return line.substring(62, 66).trim();
    }


}