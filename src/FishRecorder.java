import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.*;
import  java.util.Scanner;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;


public class FishRecorder {
    /*
    1) Read from file
    2) Create stack from file
    3) Option to add a fish
    4) Print general stats
        a. Total fish
            i. per person
            ii. per species
            iii. jig vs set
        b. Average length
            i. per person
            ii. jig vs set
        c. Longest fish
            i. per person
            ii. jig vs set
        d. Percentage of jig vs set
            i. per person
        e. Percentage of each species
            i. per person
            ii. jig vs set
     5) Save data
     */
    private Stack1Gen <String[]> stack;
    private NGen head = null;


    public FishRecorder(){
        stack = new Stack1Gen<>();
    }

    public void readFromFile(String fileName) throws FileNotFoundException {
        File f = new File(fileName);
        Scanner myScan = new Scanner(f);

        while (myScan.hasNextLine()){
            String str = myScan.nextLine();
            String[] temp = str.split(",");
            stack.push(temp);
        } //while

        myScan.close();
        stack.reverse(stack.top());
    }


    public void writeToFile(String fileName) throws FileNotFoundException {
        File f = new File(fileName);
        PrintWriter p = new PrintWriter(f);
        while(!stack.isEmpty()) {
            String[] dataArr =stack.pop();
            String dataStr = "";
            for (int i = 0; i<dataArr.length; i++) {
                if(i<dataArr.length-1) {
                    dataStr += (dataArr[i] + ",");
                } else {
                    dataStr += (dataArr[i]);
                }
            }
            p.println(dataStr);
        }
        p.close();
    }

    public void createSpreadsheet(String fileName) throws FileNotFoundException {
        File f = new File(fileName);
        PrintWriter p = new PrintWriter(f);
        NGen temp = stack.top();
        while(temp!=null) {
            String[] dataArr = (String[]) temp.getData();
            String dataStr = "";
            for (int i = 0; i<dataArr.length; i++) {
                if(i<dataArr.length-1) {
                    dataStr += (dataArr[i] + ",");
                } else {
                    dataStr += (dataArr[i]);
                }
            }
            p.println(dataStr);
            temp = temp.getNext();
        }
        p.close();
    }


    public void printFish(){
        NGen temp = stack.top();
        for (int ind = 0; ind<stack.size(); ind++){
            String[] mid = (String[]) temp.getData();
            for (int i = 0; i<mid.length; i++) {
                System.out.print(mid[i]);
            }
            System.out.println();
            temp = temp.getNext();
        }
        System.out.println(stack.size());
    }

    public double averageLength(){
        NGen temp = stack.top();
        double sum = 0;
        double count = 0;
        while(temp != null){
            String[] x  = (String[]) temp.getData();
            double i = Double.parseDouble(x[2]);
            sum += i;
            count++;
            temp = temp.getNext();
        }
        return sum/count;
    }
    public double longestFish(){
        NGen temp = stack.top();
        double longest = 0;
        while(temp != null){
            String[] x  = (String[]) temp.getData();
            double i = Double.parseDouble(x[2]);
            if(i>longest){
                longest = i;
            }
            temp = temp.getNext();
        }
        return longest;
    }
    public double shortestFish(){
        NGen temp = stack.top();
        double shortest = 0;
        while(temp != null){
            String[] x  = (String[]) temp.getData();
            double i = Double.parseDouble(x[2]);
            if(i<shortest || shortest == 0){
                shortest = i;
            }
            temp = temp.getNext();
        }
        return shortest;
    }
    public int numKeepers(char ch){
        int count = 0;
        String standard;
        boolean general = true;
        NGen temp = stack.top();
        if(ch != 'g'){
            general=false;
        } else if(ch == 's'){
            general=false;
        }
        if(general){
            while(temp!=null) {
                String[] data = (String[]) temp.getData();
                if(data[5].equals("Keeper")) count++;
                temp = temp.getNext();
            }
        } else {
            if(ch == 'w'){
                standard="Walleye";
            } else {
                standard = "Sauger";
            }
            while(temp!=null){
                String[] data = (String[]) temp.getData();
                if(data[5].equals("Keeper") && data[1].equals(standard)) count++;
                temp = temp.getNext();
            }
        }

        return count;
    }
    public int fishPerPerson(String s){
        int count = 0;
        NGen temp = stack.top();
        while(temp!=null){
            String[] arr = (String[]) temp.getData();
            if(arr[0].equals(s)){
                count++;
            }
            temp = temp.getNext();
        }
        return count;
    }
    public int fishPerMethod(String s){
        int count = 0;
        NGen temp = stack.top();
        while(temp!=null){
            String[] arr = (String[]) temp.getData();
            if(arr[3].equals(s)){
                count++;
            }
            temp = temp.getNext();
        }
        return count;
    }

    public void printStats(){
        System.out.println("----------------------------");
        System.out.println("Current Stats:");
        System.out.println("----------------------------");
        System.out.println("Average Length: " + averageLength() + '"');
        System.out.println("Longest Fish: " + longestFish() + '"');
        System.out.println("Shortest Fish: " + shortestFish() + '"');
        System.out.println("Total Fish: " +  stack.size());
        System.out.println("\tPer Person:");
        System.out.println("\t\tTim: " + fishPerPerson("Tim"));
        System.out.println("\t\tChris: " + fishPerPerson("Chris"));
        System.out.println("\t\tDad: " + fishPerPerson("Dad"));
        System.out.println("----------------------------");
        System.out.println("Keepers:");
        System.out.println("\tTotal: " + numKeepers('g'));
        System.out.println("\tSauger: " + numKeepers('s'));
        System.out.println("\tWalleye: " + numKeepers('w'));
        System.out.println("----------------------------");
        System.out.println("Method:");
        System.out.println("\tJig: " + fishPerMethod("Jig"));
        System.out.println("\tSet Rod: " + fishPerMethod("Set"));
        System.out.println("\tRattle Reel: " + fishPerMethod("Rattle"));
        System.out.println("----------------------------");
    }

    public void addFish(){
        Scanner s = new Scanner(System.in);
        boolean validInit = false;
        boolean validSpec = false;
        boolean validMethod = false;
        boolean validLength = false;
        boolean confirmed = false;
        String[] newData = new String[6];
        while(!confirmed) {
            while (!validInit) {
                System.out.println("Enter first initial");
                String str = s.nextLine();
                if (str.toLowerCase().equals("c") || str.toLowerCase().equals("t") || str.toLowerCase().equals("d")) {
                    validInit = true;
                    if (str.toLowerCase().equals("c")) {
                        newData[0] = "Chris";
                    } else if (str.toLowerCase().equals("t")) {
                        newData[0] = "Tim";
                    } else {
                        newData[0] = "Dad";
                    }
                } else {
                    System.out.println("ERROR: Invalid Input");
                }
            } // init while

            while (!validSpec) {
                System.out.println("Enter species initial");
                System.out.println("W: Walleye | S: Sauger | N: Northern");
                System.out.println("E: Eelpout | P: Perch  | F: Whitefish");
                System.out.println("O: Other");
                String str = s.nextLine();
                if (str.toLowerCase().equals("w") || str.toLowerCase().equals("s") || str.toLowerCase().equals("n") ||
                        str.toLowerCase().equals("e") || str.toLowerCase().equals("p") || str.toLowerCase().equals("f")
                        || str.toLowerCase().equals("o")) {
                    validSpec = true;
                    if (str.toLowerCase().equals("w")) {
                        newData[1] = "Walleye";
                    } else if (str.toLowerCase().equals("s")) {
                        newData[1] = "Sauger";
                    } else if (str.toLowerCase().equals("n")) {
                        newData[1] = "Northern";
                    } else if (str.toLowerCase().equals("e")) {
                        newData[1] = "Eelpout";
                    } else if (str.toLowerCase().equals("p")) {
                        newData[1] = "Perch";
                    } else if (str.toLowerCase().equals("f")) {
                        newData[1] = "Whitefish";
                    } else {
                        System.out.println("Type species name");
                        String otherS = s.nextLine();
                        newData[1] = otherS;
                    }
                } else {
                    System.out.println("ERROR: Invalid Input");
                }
            } // spec while

            while (!validLength) {
                System.out.println("Length of fish:");
                String length = s.nextLine();
                if (!length.equals("")) {
                    newData[2] = length;
                    validLength = true;
                } else {
                    System.out.println("Error. Must Enter a Length");
                }
            }

            while (!validMethod) {
                System.out.println("How was it caught?");
                System.out.println("j: Jig | s: Set rod | r: Rattle Reel");
                String str = s.nextLine();
                if (str.toLowerCase().equals("j") || str.toLowerCase().equals("s") || str.toLowerCase().equals("r")) {
                    validMethod = true;
                    if (str.toLowerCase().equals("j")) {
                        newData[3] = "Jig";
                    } else if (str.toLowerCase().equals("s")) {
                        newData[3] = "Set";
                    } else {
                        newData[3] = "Rattle";
                    }
                }
            } // method while
            System.out.println("Keeper? (y: Yes, n: No)");
            String str = s.nextLine();
            if (str.toLowerCase().equals("y")) {
                newData[5] = "Keeper";
            } else newData[5] = " ";
            LocalDateTime current = LocalDateTime.now();
            DateTimeFormatter myFormat = DateTimeFormatter.ofPattern("MM-dd | HH:mm");
            String date = current.format(myFormat);
            newData[4] = date;
            System.out.println("Is this correct?(y: yes)");
            System.out.println(newData[0] + " caught a " + newData[2] + '"' + " " + newData[1]);
            String confirm = s.nextLine();
            if (confirm.toLowerCase().equals("y")) {
                confirmed = true;
            } else  {
                validInit =  false;
                validLength = false;
                validMethod = false;
                validSpec = false;
            }
        }  // confirmed while

        stack.push(newData);
    }

    public void save() throws FileNotFoundException {
        try{
            writeToFile("src/dad_and_chris.txt");
        } catch (FileNotFoundException f){
            writeToFile("dad_and_chris.txt");
        }
        try {
            readFromFile("src/dad_and_chris.txt");
        } catch(FileNotFoundException f) {
            readFromFile("dad_and_chris.txt");
        }
    }

    public static void main(String[] args) throws FileNotFoundException {
        FishRecorder test = new FishRecorder();
        try {
            test.readFromFile("src/dad_and_chris.txt");
        } catch(FileNotFoundException f) {
            test.readFromFile("dad_and_chris.txt");
        }
        Scanner s = new Scanner(System.in);
        boolean cont = true;
        while(cont){
            boolean validInput = false;
            while(!validInput){
                System.out.println("Enter a command");
                System.out.println("n: New Fish | s: Statistics | q: Save And Quit");
                System.out.println("p: Print    | d: Delete     | c: Save And Continue");
                String str = s.nextLine();
                if(str.toLowerCase().equals("n") || str.toLowerCase().equals("s") || str.toLowerCase().equals("q")
                        || str.toLowerCase().equals("p") || str.toLowerCase().equals("d") || str.toLowerCase().equals("c")){
                    validInput = true;
                    if(str.toLowerCase().equals("n")){
                        test.addFish();
                    } else if(str.toLowerCase().equals("s")){
                        test.printStats();
                    } else if(str.toLowerCase().equals("p")){
                        test.printFish();
                    } else if(str.toLowerCase().equals("c")) {
                        test.save();
                        try{
                            test.createSpreadsheet("src/Fishing2022Results_pt2.csv");
                        } catch (FileNotFoundException f){
                            test.createSpreadsheet("Fishing2022Results_pt2.csv");
                        }
                    }else if(str.toLowerCase().equals("d")){
                        if(!test.stack.isEmpty()) {
                            test.stack.pop();
                        } else System.out.println("Error. Empty Stack");
                    } else {
                        cont = false;
                    }
                } else{
                    System.out.println("Error! Invalid Input");
                }
            } // while validInput
        } // while cont
        try{
            test.writeToFile("src/dad_and_chris.txt");
        } catch (FileNotFoundException f){
            test.writeToFile("dad_and_chris.txt");
        }
    }
}
