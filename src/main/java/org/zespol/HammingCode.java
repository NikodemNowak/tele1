package org.zespol;

import java.util.Arrays;

public class HammingCode {

    boolean[][] parityBitsControlMatrix1Bit = {{
        true, false, true, false, true, false, true, false, true, false, true, false
    }, {
        false, true, true, false, false, true, true, false, false, true, true, false
    }, {
        false, false, false, true, true, true, true, false, false, false, false, true
    }, {
        false, false, false, false ,false, false, false, true, true, true, true, true
    }};

    public boolean[] messageToCode1Bit(boolean[] message) {
        boolean[] code = new boolean[12];
        int j = 0;
        for (int i = 0; i < code.length; i++) {
            if (i == 0 || i == 1 || i == 3 || i == 7) {
                continue;
            }
            code[i] = message[j];
            j++;
        }

        code[0] = code[2] ^ code[4] ^ code[6] ^ code[8] ^ code[10];
        code[1] = code[2] ^ code[5] ^ code[6] ^ code[9] ^ code[10];
        code[3] = code[4] ^ code[5] ^ code[6] ^ code[11];
        code[7] = code[8] ^ code[9] ^ code[10] ^ code[11];

        return code;
    }

    public boolean[] calculateSyndrome1Bit(boolean[] code) {
        boolean[][] transposedMatrix = transposeMatrix(parityBitsControlMatrix1Bit);
        boolean[] resultCode = new boolean[4];
        boolean[][] temp = new boolean[code.length][transposedMatrix[0].length];

        for (int i=0; i<code.length; i++) {
            if (code[i]) {
                System.arraycopy(transposedMatrix[i], 0, temp[i], 0, transposedMatrix[i].length);
            }
        }

        for (boolean[] booleans : temp) {
            for (int j = 0; j < booleans.length; j++) {
                resultCode[j] ^= booleans[j];
            }
        }

        System.out.println("Syndrom: " + Arrays.toString(resultCode));

        return resultCode;
    }

    public void whichBitBroken1Bit(boolean[] resultCode){
        boolean[][] transposedMatrix = transposeMatrix(parityBitsControlMatrix1Bit);
        for(int i=0; i<transposedMatrix.length; i++){
            boolean[] row = transposedMatrix[i];
            boolean isBroekn = true; // Zakładamy, że bit kodu jest uszkodzony
            for(int j=0; j<row.length; j++){
                if(row[j] != resultCode[j]){
                    isBroekn = false;   // Jeśli chociaż 1 bit wiersza różni się od syndromu tzn, że nie ten bit kodu jest uszkodzony
                    break;
                }
            }
            if(isBroekn){
                System.out.println("Bit " + (i+1) + " jest uszkodzony.");
                return;
            }
        }
        System.out.println("Nie znaleziono uszkodzonego bitu.");
    }

    public static boolean[][] transposeMatrix(boolean[][] matrix){
        int m = matrix.length;
        int n = matrix[0].length;

        boolean[][] transposedMatrix = new boolean[n][m];

        for(int x = 0; x < n; x++) {
            for(int y = 0; y < m; y++) {
                transposedMatrix[x][y] = matrix[y][x];
            }
        }

        return transposedMatrix;
    }

    boolean[][] parityBitsControlMatrix2Bits = {{
            true, false, true, false, true, false, true, false, true, false, true, false, false
    }, {
            false, true, true, false, false, true, true, false, false, true, true, false, false
    }, {
            false, false, false, true, true, true, true, false, false, false, false, true, false
    }, {
            false, false, false, false ,false, false, false, true, true, true, true, true, false
    },{
            true, true, true, true, true, true, true, true, true, true, true, true, true
    }
    };

    public boolean[] messageToCode2Bits(boolean[] message) {
        boolean[] code = new boolean[13];
        int j = 0;

        // Najpierw umieść bity wiadomości w odpowiednich pozycjach
        for (int i = 0; i < code.length; i++) {
            if (i == 0 || i == 1 || i == 3 || i == 7 || i == 12) {
                continue;
            }
            code[i] = message[j];
            j++;
        }

        // Pierwsza iteracja - oblicz bity parzystości bez uwzględnienia bitu 12
        code[0] = code[2] ^ code[4] ^ code[6] ^ code[8] ^ code[10];
        code[1] = code[2] ^ code[5] ^ code[6] ^ code[9] ^ code[10];
        code[3] = code[4] ^ code[5] ^ code[6] ^ code[11];
        code[7] = code[8] ^ code[9] ^ code[10] ^ code[11];

        // Oblicz bit parzystości całkowitej
        code[12] = code[0] ^ code[1] ^ code[2] ^ code[3] ^ code[4] ^ code[5] ^ code[6] ^ code[7] ^ code[8] ^ code[9] ^ code[10] ^ code[11];

        return code;
    }

    public boolean[] calculateSyndrome2Bits(boolean[] code) { // Lepsza nazwa funkcji
        boolean[][] transposedMatrix = transposeMatrix(parityBitsControlMatrix2Bits);
        boolean[] syndrome = new boolean[5];
        boolean[][] temp = new boolean[code.length][transposedMatrix[0].length];

        for (int i=0; i<code.length; i++) {
            if (code[i]) {
                System.arraycopy(transposedMatrix[i], 0, temp[i], 0, transposedMatrix[i].length);
            }
        }

        for (boolean[] booleans : temp) {
            for (int j = 0; j < booleans.length; j++) {
                syndrome[j] ^= booleans[j];
            }
        }

        System.out.println("Syndrom: " + Arrays.toString(syndrome));
        return syndrome;
    }


    public void whichBitBroken2Bit(boolean[] syndrome){
        boolean[][] transposedMatrix = transposeMatrix(parityBitsControlMatrix2Bits);
        for(int i=0; i<transposedMatrix.length; i++){
            boolean[] row = transposedMatrix[i];
            boolean isBroekn = true;
            for(int j=0; j<row.length; j++){
                if(row[j] != syndrome[j]){
                    isBroekn = false;
                    break;
                }
            }
            if(isBroekn){
                System.out.println("Bit " + (i+1) + " jest uszkodzony.");
                return;
            }
        }
        System.out.println("Nie znaleziono 1 uszkodzonego bitu. Sprawdź czy nie ma 2 uszkodzonych bitów.");

        // Sprawdzamy kombinacje 2 bitów
        // Dla dwóch uszkodzonych bitów, syndrom będzie XORem odpowiednich wierszy macierzy
        for (int i = 0; i < transposedMatrix.length; i++) {
            for (int j = i+1; j < transposedMatrix.length; j++) {
                boolean[] combinedSyndrome = new boolean[syndrome.length];
                for (int k = 0; k < syndrome.length; k++) {
                    combinedSyndrome[k] = transposedMatrix[i][k] ^ transposedMatrix[j][k];
                }

                if (Arrays.equals(combinedSyndrome, syndrome)) {
                    System.out.println("Wiem, że 2 bity są uszkodzone, ale nie potrafię określic, które to bity.");
                    return;
                }
            }
        }

        System.out.println("Nie znaleziono 2 uszkodzonych bitów.");
    }
}
