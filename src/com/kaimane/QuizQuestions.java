/**
 * Programmer: Jacob Scott
 * Program Name: QuizQuestions
 * Description: tracks Quiz questions & answers
 * Date: Apr 5, 2011
 */
package com.kaimane;

import com.jascotty2.Rand;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.logging.Level;

/**
 * @author jacob
 */
public class QuizQuestions {

    protected ArrayList<QuizQuestion> questions = new ArrayList<QuizQuestion>();

    public boolean load(File toLoad) {
        if (toLoad.exists()) {
            BufferedReader iReader = null;
            FileReader fReader = null;
            try {
                fReader = new FileReader(toLoad.getAbsolutePath());
                iReader = new BufferedReader(fReader);
                int linenum = 0;

                String line = null;
                while ((line = iReader.readLine()) != null) {
                    ++linenum;

                    if (line.startsWith("//") || line.startsWith("#") || line.equals("")) {
                        continue;
                    }
                    QuizQuestion q = QuizQuestion.fromLine(line);
                    if (q != null) {
                        questions.add(q);
                    } else {
                        Quiz.Log(Level.WARNING, "Invalid question at line " + linenum);
                    }
                }
                return true;
            } catch (IOException exception) {
                Quiz.Log(Level.SEVERE, "Error loading quiz file", exception);
            } finally {
                try {
                    if (iReader != null) {
                        iReader.close();
                    }
                    if (fReader != null) {
                        fReader.close();
                    }
                } catch (IOException ex) {
                    //Logger.getLogger(QuizQuestions.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
        return false;
    }

    public int count() {
        return questions.size();
    }

    public QuizQuestion randomQuestion() {
        return questions.get(Rand.RandomInt(0, questions.size() - 1));
    }
} // end class QuizQuestions

