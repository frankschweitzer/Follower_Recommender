import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.Scanner;

public class FollowerRecommender {

    public static void main(String args[]) throws IOException {

        final long startTime = System.nanoTime();

        System.out.println("1024 Recommendations, check rec1024.txt");
        Scanner scnr = new Scanner(System.in);
       // System.out.println("Enter a file: ");
        //String fileName = scnr.next();
        File file = new File("src/A1024.txt");
        //System.out.println("What is the size of the matrix?");
       // int size = scnr.nextInt();
        ArrayList<ArrayList<Integer>> matrix = createAdjMatrix(file, 1024);
//        System.out.print(matrix.toString());
        ArrayList<ArrayList<Integer>> mainIndexes = bfs(matrix);
//        System.out.println(mainIndexes.toString());
//        System.out.println(twoPaths(mainIndexes).toString());

        ArrayList<Integer> followerRecIndex = new ArrayList<>();
        for (int i=0;i<twoPaths(mainIndexes).size();i++){
           // System.out.println(i+1+" follower recommender is "+(twoPaths(mainIndexes).get(i)+1));
            followerRecIndex.add((twoPaths(mainIndexes).get(i)+1));
        }

        FileWriter fileWriter = new FileWriter("src/rec1024.txt");
        for (int j =0 ;j<followerRecIndex.size();j++){
//            System.out.println(followerRecIndex.get(j));
            String num = String.valueOf(followerRecIndex.get(j))+" ";
            fileWriter.write(num+"\n");
        }
        fileWriter.close();

        checkNeighbors(matrix, 1, 4);

        System.out.println("");
        System.out.println("16 Recommendations, check rec16.txt");
        File file2 = new File("src/A16.txt");
        ArrayList<ArrayList<Integer>> matrix2 = createAdjMatrix(file2, 16);
        ArrayList<ArrayList<Integer>> mainIndexes2 = bfs(matrix2);

        ArrayList<Integer> followerRecIndex2 = new ArrayList<>();
        for (int i=0;i<twoPaths(mainIndexes2).size();i++){
            // System.out.println(i+1+" follower recommender is "+(twoPaths(mainIndexes).get(i)+1));
            followerRecIndex2.add((twoPaths(mainIndexes2).get(i)+1));
        }

        FileWriter fileWriter2 = new FileWriter("src/rec16.txt");
        for (int j =0 ;j<followerRecIndex2.size();j++){
//            System.out.println(followerRecIndex.get(j));
            String num = String.valueOf(followerRecIndex2.get(j))+" ";
            fileWriter2.write(num+"\n");
        }
        fileWriter2.close();

        final long endTime = System.nanoTime();
        long totalTime = (endTime-startTime)/1000000;


        System.out.println("The total time was: "+ totalTime);

    }

    public static ArrayList<ArrayList<Integer>> createAdjMatrix(File f, int s) throws FileNotFoundException {

        ArrayList<ArrayList<Integer>> a = new ArrayList<>();
        Scanner fileScnr = new Scanner(f);

        int j = 0;
        for (int i = 0; i < s; i++) {
            ArrayList<Integer> row = new ArrayList<Integer>();
            String currentLine = fileScnr.nextLine();
            Scanner scnr = new Scanner(currentLine);
            while (scnr.hasNext()) {
                if (scnr.nextInt() == 1) {
                    row.add(j);
                }
                j++;
            }
            a.add(row);
            j = 0;
        }
        return a;
    }


    public static ArrayList<ArrayList<Integer>> bfs(ArrayList<ArrayList<Integer>> a) {

        ArrayList<ArrayList<Integer>> indexesMatrix = new ArrayList<ArrayList<Integer>>();

        for (int i = 0; i < a.size(); i++) {

            LinkedList<Node> queue = new LinkedList<>();
            ArrayList<Integer> indexes = new ArrayList<>();
            Node n = new Node(i);
            n.distance = 0;
            queue.add(n);

            while (!queue.isEmpty()) {
                Node currentNode = queue.remove();

                for (int l = 0; l < a.get(currentNode.index).size(); l++) {
                    Node neigh = new Node(a.get(currentNode.index).get(l));
                    if (neigh.distance == -1) {
                        neigh.distance = currentNode.distance + 1;
                        if (neigh.distance < 2) {
                            queue.add(neigh);
                        }
                    }
                    if (neigh.distance == 2) {
                        //Sam's idea
                        if (!a.get(i).contains(neigh.index) && !(neigh.index == n.index)) {
                            indexes.add(neigh.index);
                        }
                    }
                }
            }
            Collections.sort(indexes);
            indexesMatrix.add(indexes);
        }

        return indexesMatrix;
    }


    public static ArrayList<Integer> twoPaths(ArrayList<ArrayList<Integer>> a) {

        ArrayList<Integer> index = new ArrayList<>();
        int max=0;
        int maxValue=0;
        int counter=0;
        int facts=0;

        for(int i=0;i<a.size();i++){

            ArrayList<Integer> current = a.get(i);
            counter =0;
            max=0;
            maxValue=0;
            facts=0;
            for (int j=0;j<current.size()-1;j++){

                if (!current.isEmpty()){
                    if (current.get(j+1)==current.get(j)){
                        facts++;
                        counter++;
                        if (counter>max){
                            max=counter;
                            maxValue=current.get(j);
                        }
                    }
                    else{
                        counter=0;
                    }
                }
                else {
                    maxValue=-1;
                }
            }

            if (current.isEmpty()){
                maxValue = -1;
            }
            if (facts==0&&!current.isEmpty()){
                maxValue=current.get(0);
            }
            index.add(maxValue);
        }
        return index;
    }
    public static void checkNeighbors(ArrayList<ArrayList<Integer>> a, int first, int rec) throws FileNotFoundException {

        File wordFile = new File("src/words.txt");
        ArrayList<String> words = new ArrayList<String>();
        Scanner wordScan = new Scanner(wordFile);

        for (int i=0;i<a.size();i++){
            words.add(wordScan.nextLine());
        }

        first-=1;
        rec-=1;
        ArrayList<Integer> indexRow = a.get(first);
        for (int i:indexRow){
            if (a.get(i).contains(rec)){
                System.out.println("A word that connects the word "+(words.get(first))+" to its recommended word "+(words.get(rec))+" is "+(words.get(i)));
//                System.out.println("Index of a word that connects word at index "+(first+1)+" to recommended word at index "+(rec+1)+" is at index "+(i+1)+" of words.txt");
            }
        }
    }
}

 class Node {

    int index;
    int distance;

    public Node(){
        this.index=0;
        this.distance=0;
    }

    public Node(int i){
        this.index=i;
        this.distance=-1;
    }

    public void setIndex(int i){
        this.index = i;
    }
    public int getIndex(){
        return this.index;
    }

    public void setDistance(int num){
        this.distance = num;
    }
    public int getDistance(){
        return this.distance;
    }

 }
