import java.util.ArrayList;
import java.util.Random;
import java.util.Scanner;

public class main {

	public static void main(String[] args) {
//		 TODO Auto-generated method stub
		System.out.println("Enter the mode ( 1 = encryption, 2 = decryption ): ");
		Scanner in = new Scanner(System.in);
		String modeS = in.nextLine();
		modeS = modeS.replaceAll(" ", "");
		if (!modeS.equals("1") && !modeS.equals("2")) {
			System.out.print("Wrong code, exiting...");
			in.close();
			System.exit(-1);
		}
		int mode = Integer.parseInt(modeS);
		
		
		// do read the input and output file
		System.out.print("Enter the input file name: ");
		String ip = in.nextLine();
		
	
		
		System.out.print("Enter the output file name: ");
		String op = in.nextLine();
		
		if (mode == 1) {
			RSA r0 = new RSA();
			int[] k0 = r0.getKey();
			String k2 = "";
			Random r = new Random();
			String k1 = "";

			for(int i = 0; i < 8; i++) {
				int m = r.nextInt((int)Math.pow(16, 2));
				String n = Integer.toHexString(m);
				for(int j = 0; j < 2-n.length();j++) {
					k1 = k1.concat("0");
				}
				k1 = k1.concat(n);
				k2 = k2.concat(r0.encryption(m, k0[0], k0[1]));
			}
			
			DES d = new DES();
			ArrayList<Integer> ai = new ArrayList<Integer>();
			for(int i = 0; i < k1.length(); i++) {
				d.converter(ai, k1.charAt(i));
			}
			for(int i = 0; i < ai.size(); i++) {
				System.out.print(ai.get(i));
			}
			System.out.println();
			System.out.println(k1);
			
			d.encryption(ip, op, ai);
			System.out.println("DONE!!!");
			System.out.println("RSA public key: " + String.valueOf(k0[0]) + " ," + String.valueOf(k0[1]));
			System.out.println("RSA private key: " + String.valueOf(k0[0]) + " ," + String.valueOf(k0[2]));
			System.out.println("RSA result: " + k2);
			
			
		}else {
			RSA r0 = new RSA();
			System.out.print("Enter the first part of key: ");
			int k = Integer.parseInt(in.nextLine());
			
			System.out.print("Enter the second part of key: ");
			int k1 = Integer.parseInt(in.nextLine());
			
			System.out.print("Enter result of RSA: ");
			String r = in.nextLine();
			
			ArrayList<Integer> m = r0.decryption(r, k, k1);
			
			String k2 = "";
			for (int i = 0; i < m.size(); i++) {
				String n = Integer.toHexString(m.get(i));
				for(int j = 0; j < 2-n.length();j++) {
					k2 = k2.concat("0");
				}
				k2 = k2.concat(n);
			}
			
			System.out.println(k2);
			DES d = new DES();
			ArrayList<Integer> ai = new ArrayList<Integer>();
			
			for(int i = 0; i < k2.length(); i++) {
				d.converter(ai, k2.charAt(i));
			}
			for(int i = 0; i < ai.size(); i++) {
				System.out.print(ai.get(i));
			}
			System.out.println();
			d.decryption(ip, op, ai);
			
			System.out.println("DONE!!!");
		}
		
		
	}

}
