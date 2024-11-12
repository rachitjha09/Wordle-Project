import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.io.File;
import java.util.Scanner;



public class Wordle implements ActionListener{
    private ArrayList<String> wordList;
    private int column=0;
    private int row=0;
    private JLabel[][] labels;
    private JButton[] alphabet;
    private JButton enter;
    private JButton delete;
    private String word;


    public Wordle(){
        JFrame frame=createFrame();
        createLabels(frame);
        createKeyboard(frame);
        frame.setVisible(true);
        wordList=getWordsFromFile();
        resetGame();
    }
    public static void main(String[] args){
        new Wordle();
    }
    private JFrame createFrame(){
        JFrame frame=new JFrame();
        frame.setLayout(new GridLayout(9, 1)); // 9 rows 1 column
        frame.setSize(900, 300);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setExtendedState(JFrame.MAXIMIZED_BOTH);
        return frame;
    }
    private void createLabels(JFrame frame){
        labels=new JLabel[6][5];
        JPanel[] panels=new JPanel[6];
        for (int i=0; i < 6; i++){
            panels[i]=new JPanel();
            panels[i].setLayout(new GridLayout(1, 5)); // 1 row 5 columns
            for (int j=0; j < 5; j++){
                labels[i][j]=new JLabel();
                labels[i][j].setBorder(BorderFactory.createLineBorder(Color.BLACK));
                labels[i][j].setHorizontalAlignment(SwingConstants.CENTER);
                labels[i][j].setFont(new Font("Arial", Font.BOLD, 24));
                labels[i][j].setOpaque(true);
                panels[i].add(labels[i][j]);
            }
            frame.add(panels[i]);
        }
    }
    private void createKeyboard(JFrame frame){
        String row1="QWERTYUIOP";
        String row2="ASDFGHJKL";
        String row3="ZXCVBNM";
        JPanel[] panels=new JPanel[3];
        for (int i=0; i < 3; i++){
            panels[i]=new JPanel();
            panels[i].setLayout(new GridLayout(1, 10)); // 1 row 10 columns
        }
        alphabet=new JButton[26];
        int index=0;
        for (int i=0; i < row1.length(); i++){
            alphabet[index]=createButton(row1.substring(i, i + 1));
            panels[0].add(alphabet[index]);
            index++;
        }
        for (int i=0; i < row2.length(); i++){
            alphabet[index]=createButton(row2.substring(i, i + 1));
            panels[1].add(alphabet[index]);
            index++;
        }
        enter=createButton("Enter");
        panels[2].add(enter);

        for (int i=0; i < row3.length(); i++){
            alphabet[index]=createButton(row3.substring(i, i + 1));
            panels[2].add(alphabet[index]);
            index++;
        }
        delete=createButton("Backspace");
        panels[2].add(delete);
        for (int i=0; i < 3; i++){
            frame.add(panels[i]);
        }
    }
    private JButton createButton(String text){
        JButton button=new JButton(text);
        button.addActionListener(this);
        return button;
    }
    @Override public void actionPerformed(ActionEvent e){
        for (JButton button : alphabet){
            if (button.equals(e.getSource())){
                update(button.getText());
            }
        }
        if (e.getSource().equals(delete)){
            backspace();
        }
        if (e.getSource().equals(enter)){
            checkWord();
        }
    }
    private void resetGame(){
        for (int r=0; r < 6; r++){
            for (int c=0; c < 5; c++){
                labels[r][c].setText("");
                labels[r][c].setBackground(Color.white);
            }
        }
        word=wordList.get((int) (Math.random() * wordList.size())).toUpperCase();
    }
    private boolean valid(){
        String str="";
        for (JLabel label : labels[row]){
            if (label.getText().equals("")){
                JOptionPane.showMessageDialog(null, "Not enough letters", "", JOptionPane.OK_OPTION);
                return false;
            } else{
                str += label.getText();
            }
        }
        if (!wordList.contains(str)){
            JOptionPane.showMessageDialog(null, "Not a valid word", "", JOptionPane.OK_OPTION);
            return false;
        }
        return true;
    }

    private void checkWord(){
        if (!valid()){
            return;
        }
        boolean b=true;
        for (int i=0; i < 5; i++){
            String letter=labels[row][i].getText();
            if (!word.contains(letter)){
                labels[row][i].setBackground(Color.gray);
                b=false;
            } else{
                if (word.indexOf(letter)==i){
                    labels[row][i].setBackground(new Color(106, 170, 100));
                } else{
                    labels[row][i].setBackground(new Color(201, 180, 88));
                    b=false;
                }
            }
        }
        if (b){
            int reply=JOptionPane.showConfirmDialog(null, "You guessed the word right!\n" +
                            "Want to play again?",
                    "", JOptionPane.YES_NO_OPTION);
            if (reply==JOptionPane.YES_OPTION){
                resetGame();
                row=0;
                column=0;
            } else{
                row=-1;
            }
        } else{
            row++;
            column=0;
        }
        if (row>5){
            int reply=JOptionPane.showConfirmDialog(null, "The word is: " + word +
                            "\nWant to try again?",
                    "", JOptionPane.YES_NO_OPTION);
            if (reply==JOptionPane.YES_OPTION){
                resetGame();
                row=0;
                column=0;
            } else{
                row=-1;
            }
        }
    }
    private ArrayList<String> getWordsFromFile(){
        try{
            File f=new File("C:\\Users\\1261124_stamfordpubl\\IdeaProjects\\Wordle Project\\src\\WordleWords");
            Scanner s=new Scanner(f);
            ArrayList<String> words=new ArrayList<>();
            while (s.hasNext()){
                words.add(s.nextLine().toUpperCase());
            }
            return words;
        } catch (Exception e){
            System.out.println(e);
            return null;
        }
    }
    private void backspace(){
        for (int i=4; i>=0; i--){
            if (!labels[row][i].getText().equals("")){
                labels[row][i].setText("");
                break;
            }
        }
        if (column>0){
            column--;
        }
    }
    private void update(String key){
        if (row<0 || row>5){
            return;
        }
        for (int i=0; i < 5; i++){
            if (labels[row][i].getText().equals("")){
                labels[row][i].setText(key);
                if (column < 5){
                    column++;
                }
                return;
            }
        }
    }
}

