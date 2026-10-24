public class UltimateTicTacToeGame {

    //Box Numbers, Row Numbers, and Column Numbers are always indexed from 1 - 9. Only indexed 0 - 8 when acessing the array (but usually automatically converted by the code).

    //Instance Variables
    private String[][] board;
    private String[][] unavailableBoxes;
    private boolean placeAnywhere;

    //Constructor
    public UltimateTicTacToeGame() {
        board = new String[9][9];
        unavailableBoxes = new String[9][2];
        placeAnywhere = true;

    }

    //Sets the array of the board
    public void setBoard(String[][] board) {
        this.board = board;
    }

    //Returns the array of the board
    public String[][] getBoard() {
        return board;
    }

    //Changes the player's ability to move anywhere using the given boolean. Given boolean of true = yes the player can play anywhere.
    public void setPlaceAnywhere(boolean placeAnywhere) {
        this.placeAnywhere = placeAnywhere;
    }

    //Checks if the player can make their move anywhere
    public boolean canPlaceAnywhere() {
        return placeAnywhere;
    }

    public int getValidBox(char[] move) {
        String strmove = new String(move);
        for (int h = 1; h <= 9; h++) {
            String[][] box = generateBoxArray(h);
            for (int i = 0; i < 3; i++) {
                for (int j = 0; j < 3; j++) {
                    if (box[i][j].equals(strmove)) {
                        //System.out.println(box[i][j]);
                        return (i * 3) + j + 1;

                    }
                }
            }
        }
        return 0;
    }

    //Adds unavailable boxes
    public void addUnavailableBox(int boxNum, String player) {
        for (int i = 0; i < 9; i ++) {
            if (unavailableBoxes[i][0] == null) {
                unavailableBoxes[i][0] = ("" + boxNum);
                unavailableBoxes[i][1] = player;
                return;
            }
        }
    }

    //Returns whether or not a box is unavailable. Returns true if it is unavailable.
    public boolean isUnavailableBox(int boxNum) {
        for (int i = 0; i < 9; i ++) {
            if (("" + boxNum).equals(unavailableBoxes[i][0])) {
                return true;
            }
        }

        return false;
    }

    //Creates an array of possible values in row column that would correspond the given box number (both are indexed 1-9)
    public static String[][] generateBoxArray(int boxNum) {

        String[][] boxArr = new String[3][3];

        int rowOffset = ((boxNum-1)/3) *3;
        int columnOffset = ((boxNum-1)%3) * 3;

        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                boxArr[i][j] = (1+rowOffset+i) + " " + (1+columnOffset+j);
                //System.out.println(boxArr[i][j]);
            }
        }

        return boxArr;

    }

    //Checks if all the spots are full within a box. Automatically adds the box to the unavailableBox list and returns true if the box is full.
    public boolean checkBoxFull(int boxNum) {

        if (boxNum < 1 || boxNum > 9) {
            return true;
        }

        if (isUnavailableBox(boxNum)) {
            return true;
        }

        String[][] box = generateBoxArray(boxNum);


        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                //String posStr = box[i][j];
                char[] posArr = box[i][j].toCharArray();
                int row = posArr[0] - '0';
                int column = posArr[2] - '0';

                if (board[row-1][column-1] == null) {
                    //System.out.println(box[i][j]);
                    return false;

                }
            }
        }
        addUnavailableBox(boxNum, "TIE");
        return true;
    }

    //Returns an int value of the box number given the int value of the row and column (indexed 1 - 9 for both input and output)
    public int getBoxNum(int row, int column) {

        if (row == 1 || row == 2 || row == 3) {
            if (column == 1 || column == 2 || column == 3) {
                return 1;
            } else if (column == 4 || column == 5 || column == 6) {
                return 2;
            } else if (column == 7 || column == 8 || column == 9) {
                return 3;
            }
        } else if (row == 4 || row == 5 || row == 6) {
            if (column == 1 || column == 2 || column == 3) {
                return 4;
            } else if (column == 4 || column == 5 || column == 6) {
                return 5;
            } else if (column == 7 || column == 8 || column == 9) {
                return 6;
            }
        } else if (row == 7 || row == 8 || row == 9) {
            if (column == 1 || column == 2 || column == 3) {
                return 7;
            } else if (column == 4 || column == 5 || column == 6) {
                return 8;
            } else if (column == 7 || column == 8 || column == 9) {
                return 9;
            }
        }

        return 0;
    }


    //Checks if a given player has won a given box. Returns true if the given player won the given box.
    public boolean isBoxWinner(String player, int boxNum) {

        if (boxNum < 1 || boxNum > 9) return false;

        if (isUnavailableBox(boxNum)) return false;


        int rowStart = ((boxNum-1) / 3) * 3;
        int columnStart = ((boxNum-1) % 3) * 3;


        //Horizontal
        for (int i = 0; i < 3; i++) {
            if (player.equals(board[rowStart+i][columnStart]) && player.equals(board[rowStart+i][columnStart+1]) && player.equals(board[rowStart+i][columnStart+2])) {
                addUnavailableBox(boxNum, player);
                return true;
            }
        }

        //Vertical
        for (int j = 0; j < 3; j++) {
            if (player.equals(board[rowStart][j+columnStart]) && player.equals(board[rowStart+1][j+columnStart]) && player.equals(board[rowStart+2][j+columnStart])) {
                addUnavailableBox(boxNum, player);
                return true;
            }
        }

        //Diagonal
        if (player.equals(board[rowStart+1][columnStart+1])) {
            if (player.equals(board[rowStart][columnStart]) && player.equals(board[rowStart + 2][columnStart + 2])) {
                addUnavailableBox(boxNum, player);
                return true;
            } else if (player.equals(board[rowStart][columnStart + 2]) &&  player.equals(board[rowStart + 2][columnStart])) {
                addUnavailableBox(boxNum, player);
                return true;
            }
        }


        return false;
    }

    public String getBoxWinner(int boxNum) {
        int rowStart = ((boxNum - 1) / 3) * 3;
        int columnStart = ((boxNum - 1) % 3) * 3;

        // Horizontal
        for (int i = 0; i < 3; i++) {
            if (board[rowStart + i][columnStart] != null &&
                    board[rowStart + i][columnStart].equals(board[rowStart + i][columnStart + 1]) &&
                    board[rowStart + i][columnStart].equals(board[rowStart + i][columnStart + 2])) {
                return board[rowStart + i][columnStart];
            }
        }
        // Vertical
        for (int j = 0; j < 3; j++) {
            if (board[rowStart][columnStart + j] != null &&
                    board[rowStart][columnStart + j].equals(board[rowStart + 1][columnStart + j]) &&
                    board[rowStart][columnStart + j].equals(board[rowStart + 2][columnStart + j])) {
                return board[rowStart][columnStart + j];
            }
        }
        // Diagonal
        if (board[rowStart + 1][columnStart + 1] != null && board[rowStart][columnStart] != null && board[rowStart][columnStart + 2] != null ) {
            if (board[rowStart][columnStart].equals(board[rowStart + 1][columnStart + 1]) &&
                    board[rowStart + 1][columnStart + 1].equals(board[rowStart + 2][columnStart + 2])) {
                return board[rowStart + 1][columnStart + 1];
            }
            if (board[rowStart][columnStart + 2].equals(board[rowStart + 1][columnStart + 1]) &&
                    board[rowStart + 1][columnStart + 1].equals(board[rowStart + 2][columnStart])) {
                return board[rowStart + 1][columnStart + 1];
            }
        }
        return null;
    }

    //Checks if the given player has won by using the unavailableBoxes array to see what boxes the player has won and if any of those won boxes are three in a row.
    public boolean isWinner(String player) {

        String listBoxes = "";

        for (int i = 0; i < 9; i++) {
            if (unavailableBoxes[i][0] != null && player.equals(unavailableBoxes[i][1])) {
                listBoxes += unavailableBoxes[i][0] + " ";
            }
        }

        if (listBoxes.isBlank()) {
            return false;
        }

        //Horizontal
        if (listBoxes.contains("1") && listBoxes.contains("2") && listBoxes.contains("3")) {
            return true;
        } else if (listBoxes.contains("4") && listBoxes.contains("5") && listBoxes.contains("6")) {
            return true;
        } else if (listBoxes.contains("7") && listBoxes.contains("8") && listBoxes.contains("9")) {
            return true;
        }

        //Vertical
        if (listBoxes.contains("1") && listBoxes.contains("4") && listBoxes.contains("7")) {
            return true;
        } else if (listBoxes.contains("2") && listBoxes.contains("5") && listBoxes.contains("8")) {
            return true;
        } else if (listBoxes.contains("3") && listBoxes.contains("6") && listBoxes.contains("9")) {
            return true;
        }

        //Diagonal
        if (listBoxes.contains("1") && listBoxes.contains("5") && listBoxes.contains("9")) {
            return true;
        } else if (listBoxes.contains("3") && listBoxes.contains("5") && listBoxes.contains("7")) {
            return true;
        }

        return false;
    }


    //Prints out the board in the Ultimate Tic Tac Toe Style
    public String toString() {
        String result = "";
        int count = 0;
        for (int i = 0; i < board.length; i ++) {

            if (i % 3 == 0 && i != 0) {
                result += "\n";
                for (int j = 0; j < 53; j ++) {
                    result += "█";
                }
            }
            else if (i % 1 == 0 && i != 0) {
                result += "\n";
                for (int j = 0; j < 54; j ++) {
                    if (j % 18 == 0 && j != 0) {
                        result += "█";
                    } else {
                        if (j != 0) {
                            result += "─";
                        }
                    }
                }
            }

            for (int j = 0; j < board.length; j ++) {

                if (j % 9 == 0) {
                    result += "\n";
                }
                else if (j % 3 == 0) {
                    result += " █";
                }
                else if (j % 1 == 0) {
                    result += " |";
                }
                //result += " ";
                result += " ";
                if (board[i][j] != null) {
                    result += board[i][j];
                } else {
                    result += "   ";
                }
            }
        }
        return result;
    }


}
