import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.nio.file.*;
import java.util.*;
import java.io.IOException;

public class rsa
{
    int MultiplicativeInverse(int a, int m)
    {
        for (int x = 1; x < m; x++)
            if (((a%m) * (x%m)) % m == 1)
                return x;
        return 1;
    }

    int Gcd(int e,int z)
    {
        if(e == 0)
        {
            return z;
        }
        else
        {
            return Gcd(z % e, e);
        }
    }

    int Power(int a,int b,int n)
    {
        if(b==1)
        {
            return a;
        }
        return a*(Power(a,b-1,n)%n);
    }


    ArrayList<Integer> KeyGeneration(int p, int q)
    {
        ArrayList<Integer> keys = new ArrayList<>();

        int e,d;
        int n = p * q;
        int z = (p-1) * (q-1);

        for(e=2; e<z; e++)
        {
            if(Gcd(e,z) == 1)
            {
                break;
            }
        }
        d = MultiplicativeInverse(e,z);


//        System.out.println("public key : {" + e + "," + n + "}");
//        System.out.println("private key : {" + d + "," + n + "}");
        keys.add(n);
        keys.add(e);
        keys.add(d);
        return (keys);
    }


    String Encryption(String password, int n,int e)
    {
//        System.out.println("password : " + password);
//        System.out.println("e - value : " + e);
//        System.out.println("n - value : " + n);
        StringBuilder str = new StringBuilder();
        for (int i = 0; i < password.length(); i++)
        {
            int c = password.charAt(i);
//            System.out.println("c - value : " + c);
            int cipher = (int)(Math.pow(c,e) % n);
//            System.out.println("cipher - value : " + cipher);
            char ch = (char) (cipher);
//            System.out.println("ch - value : "+ ch);
            str.append(ch);
        }
        System.out.println("Encrypted String :"+ str);
        return (str.toString());
    }


    String Decryption(String password, int n,int d)
    {
//        System.out.println("password : " + password);
//        System.out.println("d - value : " + d);
//        System.out.println("n - value : " + n);
        StringBuilder str = new StringBuilder();
        for (int i = 0; i < password.length(); i++)
        {
            int c = password.charAt(i);
//            System.out.println("c : d - value : " + c + ":" + d);
//            System.out.println("pow(c,d) : " + Power(c,d,n));
            int cipher = (Power(c, d, n) % n);
//            System.out.println("cipher - value : " + cipher);
            char ch = (char) (cipher);
//            System.out.println("ch - value : "+ ch);
            str.append(ch);
        }
        System.out.println("Decrypted String:"+ str);
        return str.toString();
    }

    void EncryptionUI(int n, int e) throws IOException
    {
        Scanner sc = new Scanner(System.in);

        System.out.println("Input Data: ");
        String data = sc.nextLine();
        data = "\n" + data;

        System.out.println("Input Password: ");
        String password = sc.nextLine();

        String encryptedMessage = Encryption(password,n,e);

        try
        {
            FileWriter fw = new FileWriter("D:\\Major Project\\file.txt");
            fw.write(encryptedMessage);
            fw.write(data);
            fw.close();
            System.out.println("File saved successfully");
        }
        catch (FileNotFoundException error)
        {
            error.printStackTrace();
        }

    }


    void DecryptionUI(int n, int d)
    {
        Scanner sc = new Scanner(System.in);
        System.out.println("Enter the password.");
        String password = sc.nextLine();
        try
        {
            Path p = Paths.get("D:\\Major Project\\file.txt");
            long lineCount = Files.lines(p).count();
            String dPassword = Files.readAllLines(p).get(0);
            System.out.println(dPassword + ": " + dPassword.length());

            String DecryptedMessage = Decryption(dPassword,n,d);

            if(DecryptedMessage.equals(password))
            {
                for(int i = 1; i<lineCount; i++)
                {
                    System.out.println(Files.readAllLines(p).get(i));
                }
            }
            else
            {
                System.out.println("Incorrect Password");
            }
        }
        catch(Exception error)
        {
            System.out.println(error.getMessage());
        }
    }

    public static void main(String[] args) throws IOException
    {
        int p = 17;
        int q = 13;

        rsa obj = new rsa();
        ArrayList<Integer> keys = obj.KeyGeneration(p,q);

        int n = keys.get(0), e = keys.get(1), d = keys.get(2);

        Scanner sc = new Scanner(System.in);
        System.out.println("1. Upload File\n2. Download File\n3. Exit");
        System.out.println("Choose the action you want to perform");
        int action = sc.nextInt();
        while(action != 0)
        {
            switch (action)
            {
                case 1:
                    obj.EncryptionUI(n,e);
                    break;
                case 2:
                    obj.DecryptionUI(n,d);
                    break;
                case 3:
                    System.exit(0);
                    break;
                default:
                    System.out.println("Invalid option");
                    break;
            }
            System.out.println("1. Upload File\n2. Download File\n3. Exit");
            System.out.println("Choose the action you want to perform");
            action = sc.nextInt();
        }

    }

}