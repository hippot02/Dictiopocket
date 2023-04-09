package com.example.pocketworld;

/*
Cette classe défini la structure de la base de données pour le quizz.
Elle contient les colonnes suivantes :
id: un identifiant unique pour chaque question
question: la question posée dans le quizz
rep1 et rep2: deux réponses possibles à la question
rep: la réponse correcte à la question
 */
public class DBContract {
    public static class Form {
        public static final String TABLE_NAME = "quizz";
        public static final String ID = "id";
        public static final String COLUMN_QUESTION = "question";
        public static final String COLUMN_REP1 = "rep1";
        public static final String COLUMN_REP2 = "rep2";
        public static final String COLUMN_REP = "rep";


    }
}
