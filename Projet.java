import java.math.BigInteger;
import java.security.MessageDigest;
import java.util.HashSet;
import java.util.List;
public class Projet{

    public static void main(String[] args) {
        // Exemple de mots de passe à tester
        String password1 = "omar";
        String password2 = "hello";
        String password3 = "123456";

        // Créer des hash MD5 de ces mots de passe
        String hash1 = md5(password1);
        String hash2 = md5(password2);
        String hash3 = md5(password3);

        // Afficher les hash pour vérification
        System.out.println("Hash 1: " + hash1);
        System.out.println("Hash 2: " + hash2);
        System.out.println("Hash 3: " + hash3);

        // Créer une instance de BruteforceLocalCracker et tester
        BruteforceLocalCracker bruteforcer = new BruteforceLocalCracker();
        System.out.println("Testing Bruteforce Cracker:");
        bruteforcer.crackPassword(hash1);  // Devrait réussir pour des mots courts avec force brute

        // Créer une instance de DictionaryLocalCracker avec un petit dictionnaire
        List<String> dictionary = List.of("hello", "world", "password", "123456", "admin");
        DictionaryLocalCracker dictionaryCracker = new DictionaryLocalCracker(dictionary);
        System.out.println("Testing Dictionary Cracker:");
        dictionaryCracker.crackPassword(hash2);  // Devrait réussir avec une attaque de dictionnaire
        dictionaryCracker.crackPassword(hash3);  // Devrait aussi réussir
    }

    
public static abstract class PasswordCrackerFactory {
  public abstract PasswordCracker createPasswordCracker();
    
}

// Interface pour les différents types de craquage de mots de passe
interface PasswordCracker {
    void crackPassword(String hashedPassword);
}

public static String md5(String input) {
    try {
        // Créer un objet MessageDigest pour l'algorithme MD5
        MessageDigest md = MessageDigest.getInstance("MD5");
        // Calculer le hash du message
        byte[] messageDigest = md.digest(input.getBytes());
        // Convertir le tableau de bytes en représentation hexadécimale
        BigInteger no = new BigInteger(1, messageDigest);
        String hashtext = no.toString(16);
        // Ajouter des zéros initiaux pour garantir une longueur de 32 caractères
        while (hashtext.length() < 32) {
            hashtext = "0" + hashtext;
        }
        return hashtext;
    } catch (Exception e) {
        throw new RuntimeException(e);
    }
}


public static abstract class LocalCracker extends PasswordCrackerFactory {
    
    public PasswordCracker createPasswordCracker(){
        return (PasswordCracker) this;
    };
}


public static class BruteforceLocalCracker extends LocalCracker {

   
      public void  crackPassword(String hashedpassword){
            bruteForceCrack(hashedpassword);  
        };
      
    

    public static void bruteForceCrack(String hashedPassword) {
        System.out.println("Cracking password by brute force...");
        // Longueur maximale du mot de passe à générer pour le craquage par force brute
        int maxLength = 5;

        for (int length = 1; length <= maxLength; length++) {
            bruteForceHelper(hashedPassword, "", length);
        }
    }

    private static void bruteForceHelper(String hashedPassword, String attempt, int length) {
        // Caractères possibles pour le craquage par force brute
        char[] charset = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789".toCharArray();

        if (length == 0) {
            // Calculer le hash MD5 de la tentative
            String hashedAttempt = md5(attempt);
            if (hashedAttempt.equals(hashedPassword)) {
                System.out.println("Password cracked by brute force: " + attempt);
                return;
            }
        } else {
            for (char c : charset) {
                bruteForceHelper(hashedPassword, attempt + c, length - 1);
            }
        }
    }
    
}


public static class DictionaryLocalCracker extends LocalCracker {

    private List<String> dictionary;

        public DictionaryLocalCracker(List<String> dictionary) {
            this.dictionary = dictionary;
        }

        public List<String> getDictionary() {
            return dictionary;
        }

    public void crackPassword(String hashedpassword){
        dictionaryAttack(hashedpassword);
        
    }

    

                                                     
    

    public  void dictionaryAttack(String hashedPassword) {
        if (dictionary == null) {
            System.out.println("Default dictionary is not set.");
            return;
        }

        dictionaryAttack(hashedPassword, dictionary);
    }

      public void dictionaryAttack(String hashedPassword, List<String> customDictionary) {
             System.out.println("Cracking password using dictionary...");
    
            // Précalculez les hachages du dictionnaire
            HashSet<String> hashedDictionary = new HashSet<>();
            for (String word : customDictionary) {
                hashedDictionary.add(md5(word));
            }
            
            // Recherche dans le dictionnaire
            for (String hashedWord : hashedDictionary) {
                if (hashedWord.equals(hashedPassword)) {
                    System.out.println("Password cracked using dictionary.");
                    return;
                }
            }
            
            System.out.println("Password not found in dictionary.");
        }

}




}