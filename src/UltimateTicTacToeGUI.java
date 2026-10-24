import javax.swing.*;
import java.awt.*;

public class UltimateTicTacToeGUI {

    //Game state tracking
    private static UltimateTicTacToeGame game;
    private static boolean isXTurn = true;
    private static char[] lastSpotArr = {0, 0, 0};
    private static boolean firstTurn = true;
    private static JLabel statusLabel;
    private static JButton[][][][] buttons;

    public static void createAndShowGUI() {
        game = new UltimateTicTacToeGame();

        JFrame frame = new JFrame("Ultimate Tic Tac Toe");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(700, 750);
        frame.setLayout(new BorderLayout());

        //Top Status Panel to display whose turn it is or game updates
        JPanel topPanel = new JPanel();
        statusLabel = new JLabel("Player X's Turn. Play anywhere!");
        statusLabel.setFont(new Font("Arial", Font.BOLD, 14));
        topPanel.add(statusLabel);
        frame.add(topPanel, BorderLayout.NORTH);

        //Main board panel holding the 3x3 grid of local boards
        JPanel mainBoardPanel = new JPanel(new GridLayout(3, 3, 6, 6));
        mainBoardPanel.setBackground(Color.DARK_GRAY);
        mainBoardPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        buttons = new JButton[3][3][3][3];

        //Loop through the 3x3 big boards
        for (int bigRow = 0; bigRow < 3; bigRow++) {
            for (int bigCol = 0; bigCol < 3; bigCol++) {

                JPanel localBoard = new JPanel(new GridLayout(3, 3, 2, 2));
                localBoard.setBorder(BorderFactory.createLineBorder(Color.BLACK, 3));
                localBoard.setBackground(Color.WHITE);

                //Loop through 3x3 cells inside each local board
                for (int smallRow = 0; smallRow < 3; smallRow++) {
                    for (int smallCol = 0; smallCol < 3; smallCol++) {
                        JButton cell = new JButton();
                        cell.setFont(new Font("Arial", Font.BOLD, 22));
                        cell.setFocusPainted(false);

                        buttons[bigRow][bigCol][smallRow][smallCol] = cell;

                        //Final coordinates for event listener capture
                        final int br = bigRow;
                        final int bc = bigCol;
                        final int sr = smallRow;
                        final int sc = smallCol;

                        cell.addActionListener(e -> handleCellClick(br, bc, sr, sc));

                        localBoard.add(cell);
                    }
                }
                mainBoardPanel.add(localBoard);
            }
        }

        frame.add(mainBoardPanel, BorderLayout.CENTER);

        //Initial highlight setup for the first turn (play anywhere)
        refreshBoardHighlights();

        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }

    private static void handleCellClick(int bigRow, int bigCol, int smallRow, int smallCol) {
        String currentPlayer = isXTurn ? " X " : " O ";
        String playerSymbol = isXTurn ? "X" : "O";

        //Convert UI indices (0-2) into backend row/column (1-9)
        int targetRow = (bigRow * 3) + smallRow + 1;
        int targetCol = (bigCol * 3) + smallCol + 1;

        int currentBoxNum = game.getBoxNum(targetRow, targetCol);

        //Check if player is forced into a specific valid box from the previous turn
        int validBox = 0;
        if (!firstTurn) {
            validBox = game.getValidBox(lastSpotArr);
            if (game.checkBoxFull(validBox)) {
                game.setPlaceAnywhere(true);
            }
        }

        //Validate rules matching console code logic
        if (!game.canPlaceAnywhere()) {
            if (currentBoxNum != validBox) {
                statusLabel.setText("INVALID: Must play in Box #" + validBox + "!");
                return;
            }
        }

        String[][] gameBoardArr = game.getBoard();
        if (gameBoardArr[targetRow - 1][targetCol - 1] != null || game.isUnavailableBox(currentBoxNum)) {
            statusLabel.setText("INVALID MOVE: Spot is taken or box is closed!");
            return;
        }

        //Apply move to backend data structure
        gameBoardArr[targetRow - 1][targetCol - 1] = currentPlayer;
        game.setBoard(gameBoardArr);

        //Update UI Button Text
        buttons[bigRow][bigCol][smallRow][smallCol].setText(playerSymbol);

        //Check if this move wins the local sub-box
        if (game.isBoxWinner(currentPlayer, currentBoxNum)) {
            statusLabel.setText(playerSymbol + " won Box #" + currentBoxNum + "!");

            if (game.isWinner(currentPlayer)) {
                JOptionPane.showMessageDialog(null, playerSymbol + " HAS WON THE ULTIMATE GAME!");
                System.exit(0);
            }
        }

        //Prepare for the next turn
        game.setPlaceAnywhere(false);
        lastSpotArr[0] = (char) (targetRow + '0');
        lastSpotArr[1] = ' ';
        lastSpotArr[2] = (char) (targetCol + '0');
        firstTurn = false;

        //Switch player turn
        isXTurn = !isXTurn;
        String nextPlayerStr = isXTurn ? "X" : "O";

        // Calculate next required box restriction and update message status
        int nextBox = game.getValidBox(lastSpotArr);
        if (game.checkBoxFull(nextBox)) {
            game.setPlaceAnywhere(true);
            statusLabel.setText("Player " + nextPlayerStr + "'s turn. Next box is full — Play anywhere!");
        } else {
            statusLabel.setText("Player " + nextPlayerStr + "'s turn. Must play in Box #" + nextBox);
        }

        //Refresh board highlights for the new player's turn
        refreshBoardHighlights();
    }

    //Automatically colors and enables/disables cells based on game rules and valid moves
    private static void refreshBoardHighlights() {
        int validBox = -1;
        if (!firstTurn) {
            validBox = game.getValidBox(lastSpotArr);
            if (game.checkBoxFull(validBox)) {
                game.setPlaceAnywhere(true);
            }
        }

        for (int bigRow = 0; bigRow < 3; bigRow++) {
            for (int bigCol = 0; bigCol < 3; bigCol++) {
                int boxNum = (bigRow * 3) + bigCol + 1;

                String winner = game.getBoxWinner(boxNum);

                for (int smallRow = 0; smallRow < 3; smallRow++) {
                    for (int smallCol = 0; smallCol < 3; smallCol++) {
                        JButton cell = buttons[bigRow][bigCol][smallRow][smallCol];
                        String[][] boardArr = game.getBoard();
                        int targetRow = (bigRow * 3) + smallRow + 1;
                        int targetCol = (bigCol * 3) + smallCol + 1;

                        //Check if the local box was won by X or O or if it was a tie and sets the corresponding color.
                        if (" X ".equals(winner)) {
                            cell.setBackground(new Color(255, 182, 193)); //Light Blue
                            cell.setEnabled(false);
                            continue;
                        }
                        else if (" O ".equals(winner)) {
                            cell.setBackground(new Color(173, 216, 230)); //Light Pink
                            cell.setEnabled(false);
                            continue;
                        }
                        else if (game.checkBoxFull(boxNum)) {
                            cell.setBackground(Color.LIGHT_GRAY);
                            cell.setEnabled(false);
                            continue;
                        }

                        //If individual cell is already taken within an active box
                        if (boardArr[targetRow - 1][targetCol - 1] != null) {
                            cell.setBackground(new Color(245, 245, 245));
                            cell.setEnabled(false);
                            continue;
                        }

                        //If cell is empty and available then apply valid move highlights
                        cell.setEnabled(true);
                        if (game.canPlaceAnywhere()) {
                            cell.setBackground(new Color(172, 225, 175)); //Soft Green Highlight (Play Anywhere)
                        } else {
                            if (boxNum == validBox) {
                                cell.setBackground(new Color(172, 225, 175)); //Soft Green Highlight (Target Box)
                            } else {
                                cell.setBackground(new Color(245, 245, 245)); //Default gray/white
                            }
                        }
                    }
                }
            }
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(UltimateTicTacToeGUI::createAndShowGUI);
    }
}