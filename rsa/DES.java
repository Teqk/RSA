//cpsc 3730 assignemnt 1
//zuoyu chen (001223172)
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.Scanner;

public class DES {

	
	
	
   public void decryption(String ip, String op,ArrayList<Integer> key) {
	   try {
			FileInputStream fis = new FileInputStream(ip);
			FileOutputStream fos = new FileOutputStream(op);
			
			byte[] bytes = new byte[1];
			int numBytes;
			String hex;
			Boolean done = false;
			Boolean check2 =false;
			ArrayList<String> m = new ArrayList<String>();
			while (!done){

			   // read file and change byte to hexadicimal if not enough byte do the fill staff 
				hex = "";
				for(int i = 0; i < 8; i ++) {
					if((numBytes = fis.read(bytes)) != -1){
						for (byte b : bytes) {
				            hex = hex.concat(String.format("%02X", b));
				        }
					}else {
						done = true;
						if (i == 0){
							check2 = true;
							break;
						}else {
							for(int j = 0; j < 8-i; j++) {
								hex = hex.concat(String.valueOf(8-i).concat(String.valueOf(8-i)));
							}
							break;
						}
					}
				}
				if (check2) {
					break;
				}

				// do DES and add the encrpted or decrypted string in to arraylist
				String out = DESWork(2, key, hex);
				m.add(out);
			}

			// if decryption check if filled, and delete them
			Boolean check = true;
			String out = m.get(m.size()-1);
			if (out.charAt(15) != '0') {
				for(int i = 0; i < 2*(Character.getNumericValue(out.charAt(15))); i++) {
					if(out.charAt(15-i) != out.charAt(15)) {
						check = false;
						break;
					}
				}
				if(check) {
					out = out.substring(0, 16-2*Character.getNumericValue(out.charAt(15)));
					m.remove(m.size()-1);
					m.add(out);
				}
			}

			
			// change hexdicimal to byte and write in the new file
			for(int k = 0; k < m.size(); k++) {
				String out1 = m.get(k);
				byte[] val = new byte[out1.length() / 2];
			      for (int i = 0; i < val.length; i++) {
			         int index = i * 2;
			         int j = Integer.parseInt(out1.substring(index, index + 2), 16);
			         val[i] = (byte) j;
			      }
				fos.write(val,0,out1.length()/2);
			}
			
			
			
			fis.close();
			fos.close();
			
		}catch(Exception e) {
			e.printStackTrace();
			System.out.println("No such file");
			System.exit(-1);
		}
   }
   
   
   public void encryption(String ip, String op,ArrayList<Integer> key) {
	   try {
			FileInputStream fis = new FileInputStream(ip);
			FileOutputStream fos = new FileOutputStream(op);
			
			byte[] bytes = new byte[1];
			int numBytes;
			String hex;
			Boolean done = false;
			Boolean check2 =false;
			ArrayList<String> m = new ArrayList<String>();
			while (!done){

			   // read file and change byte to hexadicimal if not enough byte do the fill staff 
				hex = "";
				for(int i = 0; i < 8; i ++) {
					if((numBytes = fis.read(bytes)) != -1){
						for (byte b : bytes) {
				            hex = hex.concat(String.format("%02X", b));
				        }
					}else {
						done = true;
						if (i == 0){
							check2 = true;
							break;
						}else {
							for(int j = 0; j < 8-i; j++) {
								hex = hex.concat(String.valueOf(8-i).concat(String.valueOf(8-i)));
							}
							break;
						}
					}
				}
				if (check2) {
					break;
				}

				// do DES and add the encrpted or decrypted string in to arraylist
				String out = DESWork(1, key, hex);
				m.add(out);
			}
		
			// change hexdicimal to byte and write in the new file
			for(int k = 0; k < m.size(); k++) {
				String out1 = m.get(k);
				byte[] val = new byte[out1.length() / 2];
			      for (int i = 0; i < val.length; i++) {
			         int index = i * 2;
			         int j = Integer.parseInt(out1.substring(index, index + 2), 16);
			         val[i] = (byte) j;
			      }
				fos.write(val,0,out1.length()/2);
			}
			
			
			
			fis.close();
			fos.close();
			
		}catch(Exception e) {
			e.printStackTrace();
			System.out.println("No such file");
			System.exit(-1);
		}
   }
   
   

   //main class for encryption and decryption
	public String DESWork(int mode, ArrayList<Integer> key,  String txt) {
		ArrayList<Integer> text = new ArrayList<Integer>();
		for (int i = 0; i < 16; i++) {
			converter(text, txt.charAt(i));
		}

		//check the mode and find the left shift nums
		int roundnum = 0;
		if(mode == 1) {
			roundnum = 0;
		}else if(mode == 2) {
			roundnum = 28;
		}
		
		int[] round = {1,1,2,2,2,2,2,2,1,2,2,2,2,2,2,1,0};
		ArrayList<Integer> keyLeft = new ArrayList<Integer>();
		ArrayList<Integer> keyRight = new ArrayList<Integer>();
		PC1 (keyLeft,keyRight,key);
		
		
		ArrayList<Integer> textLeft = new ArrayList<Integer>();
		ArrayList<Integer> textRight = new ArrayList<Integer>();
		IP(textLeft, textRight, text);
		
		ArrayList<Integer> expansion = new ArrayList<Integer>();
		ArrayList<Integer> roundkey = new ArrayList<Integer>();
		ArrayList<Integer> xor = new ArrayList<Integer>();
		ArrayList<Integer> a_s = new ArrayList<Integer>();
		ArrayList<Integer> p = new ArrayList<Integer>();
		ArrayList<Integer> newLeft = new ArrayList<Integer>();


		//doing encryption/ decryption
		for(int j = 0; j < 16; j++ ) {
			if (mode == 1) {
				roundnum = roundnum + round[j];
			}else {
				roundnum = roundnum - round[16-j];
			}
			EP(expansion, textRight);
			PC2(roundkey,keyLeft,keyRight,roundnum);
			for (int i = 0; i < 48; i++) {
				xor.add(roundkey.get(i)^expansion.get(i));
			}
			Sbox(a_s,xor);
			Permutation(p, a_s);
			newLeft.addAll(textRight);
			textRight.clear();
			for(int i = 0; i < 32; i ++) {
				textRight.add(textLeft.get(i)^p.get(i));
			}
			textLeft.clear();
			textLeft.addAll(newLeft);
			expansion.clear();
			roundkey.clear();
			xor.clear();
			a_s.clear();
			p.clear();
			newLeft.clear();
		}
		ArrayList <Integer> done = new ArrayList<Integer>();
		IP1(done,textRight,textLeft);
		String output = converter2(done);
		done.clear();
		text.clear();

		//output the incryped or decryped string
		return output;
	}


   // the function to add the binary of the hex string into an arraylist
	public void converter(ArrayList<Integer> x, char y) {
		switch (y) {
		case '0':
			x.add(0);
			x.add(0);
			x.add(0);
			x.add(0);
			break;
			
		case '1':
			x.add(0);
			x.add(0);
			x.add(0);
			x.add(1);
			break;
		case '2':
			x.add(0);
			x.add(0);
			x.add(1);
			x.add(0);
			break;
		case '3':
			x.add(0);
			x.add(0);
			x.add(1);
			x.add(1);
			break;
		case '4':
			x.add(0);
			x.add(1);
			x.add(0);
			x.add(0);
			break;
		case '5':
			x.add(0);
			x.add(1);
			x.add(0);
			x.add(1);
			break;
		case '6':
			x.add(0);
			x.add(1);
			x.add(1);
			x.add(0);
			break;
		case '7':
			x.add(0);
			x.add(1);
			x.add(1);
			x.add(1);
			break;
		case '8':
			x.add(1);
			x.add(0);
			x.add(0);
			x.add(0);
			break;
		case '9':
			x.add(1);
			x.add(0);
			x.add(0);
			x.add(1);
			break;
		case 'A':
		case 'a':
			x.add(1);
			x.add(0);
			x.add(1);
			x.add(0);
			break;
		case 'B':
		case 'b':
			x.add(1);
			x.add(0);
			x.add(1);
			x.add(1);
			break;
		case 'C':
		case 'c':
			x.add(1);
			x.add(1);
			x.add(0);
			x.add(0);
			break;
		case 'D':
		case 'd':
			x.add(1);
			x.add(1);
			x.add(0);
			x.add(1);
			break;
		case 'E':
		case 'e':
			x.add(1);
			x.add(1);
			x.add(1);
			x.add(0);
			break;
		case 'F':
		case 'f':
			x.add(1);
			x.add(1);
			x.add(1);
			x.add(1);
			break;
		default:
			break;
		}
	}

   
  // doing pc-1	
	public void PC1 (ArrayList<Integer> left, ArrayList<Integer> right, ArrayList<Integer> numbers) {
		int[] PC1left = {56,48,40,32,24,16,8,0,57,49,41,33,25,17,9,1,58,50,42,34,26,18,10,2,59,51,43,35};
		int[] PC1right = {62,54,46,38,30,22,14,6,61,53,45,37,29,21,13,5,60,52,44,36,28,20,12,4,27,19,11,3};
		for (int i = 0; i < PC1left.length; i++) {
			left.add(numbers.get(PC1left[i]));
			right.add(numbers.get(PC1right[i]));
		}
	}

   //doing IP
	public void IP (ArrayList<Integer> left, ArrayList<Integer> right, ArrayList<Integer> number) {
		int[] IPleft = {57,49,41,33,25,17,9,1 ,59,51,43,35,27,19,11,3,61,53,45,37,29,21,13,5,63,55,47,39,31,23,15,7};
		int[] IPright = {56,48,40,32,24,16,8,0,58,50,42,34,26,18,10,2,60,52,44,36,28,20,12,4,62,54,46,38,30,22,14,6};
		for (int i = 0; i < IPleft.length; i++) {
			left.add(number.get(IPleft[i]));
			right.add(number.get(IPright[i]));
		}
	}

   // doing expansion permutation
	public void EP (ArrayList<Integer> expansion, ArrayList<Integer> right) {
		int[] EPnum = {31,0,1,2,3,4,3,4,5,6,7,8,7,8,9,10,11,12,11,12,13,14,15,16,15,16,17,18,19,20,19,20,21,22,23,24,23,24,25,26,27,28,27,28,29,30,31,0};
		for (int i = 0; i < EPnum.length; i++) {
			expansion.add(right.get(EPnum[i]));
		}
	}

   //doing pc-2
	public void PC2 (ArrayList<Integer> roundkey, ArrayList<Integer> left, ArrayList<Integer> right, int round) {
		int[] leftkey = {13,16,10,23,0,4,2,27,14,5,20,9,22,18,11,3,25,7,15,6,26,19,12,1};
		int[] rightkey = {40,51,30,36,46,54,29,39,50,44,32,47,43,48,38,55,33,52,45,41,49,35,28,31};
		for(int i = 0; i < 24; i++) {
			roundkey.add(left.get((leftkey[i]+round)%28));
		}
		for(int i = 0; i < 24; i++) {
			roundkey.add(right.get((rightkey[i]+round)%28));
		}
	}

   //doing s-box
	public void Sbox (ArrayList<Integer> s, ArrayList<Integer> number) {
		char[] s1 = {'E','4','D','1','2','F','B','8','3','A','6','C','5','9','0','7','0','F','7','4','E','2','D','1','A','6','C','B','9','5','3','8','4','1','E','8','D','6','2','B','F','C','9','7','3','A','5','0','F','C','8','2','4','9','1','7','5','B','3','E','A','0','6','D'};
		char[] s2 = {'F','1','8','E','6','B','3','4','9','7','2','D','C','0','5','A','3','D','4','7','F','2','8','E','C','0','1','A','6','9','B','5','0','E','7','B','A','4','D','1','5','8','C','6','9','3','2','F','D','8','A','1','3','F','4','2','B','6','7','C','0','5','E','9'};
		char[] s3 = {'A','0','9','E','6','3','F','5','1','D','C','7','B','4','2','8','D','7','0','9','3','4','6','A','2','8','5','E','C','B','F','1','D','6','4','9','8','F','3','0','B','1','2','C','5','A','E','7','1','A','D','0','6','9','8','7','4','F','E','3','B','5','2','C'};
		char[] s4 = {'7','D','E','3','0','6','9','A','1','2','8','5','B','C','4','F','D','8','B','5','6','F','0','3','4','7','2','C','1','A','E','9','A','6','9','0','C','B','7','D','F','1','3','E','5','2','8','4','3','F','0','6','A','1','D','8','9','4','5','B','C','7','2','E'};
		char[] s5 = {'2','C','4','1','7','A','B','6','8','5','3','F','D','0','E','9','E','B','2','C','4','7','D','1','5','0','F','A','3','9','8','6','4','2','1','B','A','D','7','8','F','9','C','5','6','3','0','E','B','8','C','7','1','E','2','D','6','F','0','9','A','4','5','3'};
		char[] s6 = {'C','1','A','F','9','2','6','8','0','D','3','4','E','7','5','B','A','F','4','2','7','C','9','5','6','1','D','E','0','B','3','8','9','E','F','5','2','8','C','3','7','0','4','A','1','D','B','6','4','3','2','C','9','5','F','A','B','E','1','7','6','0','8','D'};
		char[] s7 = {'4','B','2','E','F','0','8','D','3','C','9','7','5','A','6','1','D','0','B','7','4','9','1','A','E','3','5','C','2','F','8','6','1','4','B','D','C','3','7','E','A','F','6','8','0','5','9','2','6','B','D','8','1','4','A','7','9','5','0','F','E','2','3','C'};
		char[] s8 = {'D','2','8','4','6','F','B','1','A','9','3','E','5','0','C','7','1','F','D','8','A','3','7','4','C','5','6','B','0','E','9','2','7','B','4','1','9','C','E','2','0','6','A','D','F','3','5','8','2','1','E','7','4','A','8','D','F','C','9','0','3','5','6','B'};
		char[][] s_box = {s1,s2,s3,s4,s5,s6,s7,s8};
		
		for (int i = 0; i < 8; i++) {
			int row = number.get(i*6)*2+number.get(i*6+5);
			int column = number.get(i*6+1)*8+number.get(i*6+2)*4+number.get(i*6+3)*2+number.get(i*6+4);
			converter(s, s_box[i][row*16+column]);
		}
	}

   //doing permutation function
	public void Permutation(ArrayList<Integer> m, ArrayList<Integer> s_a) {
		int[] p = {15,6,19,20,28,11,27,16,0,14,22,25,4,17,30,9,1,7,23,13,31,26,2,8,18,12,29,5,21,10,3,24};
		for(int i = 0; i < 32 ; i ++) {
			m.add(s_a.get(p[i]));
		}
	}

   //doing inverse initial permutation
	public void IP1 (ArrayList<Integer> d, ArrayList<Integer> r, ArrayList<Integer> l) {
		ArrayList<Integer> x = new ArrayList<Integer>();
		x.addAll(r);
		x.addAll(l);
		int[] IP_1 = {39,7,47,15,55,23,63,31,38,6,46,14,54,22,62,30,37,5,45,13,53,21,61,29,36,4,44,12,52,20,60,28,35,3,43,11,51,19,59,27,34,2,42,10,50,18,58,26,33,1,41,9,49,17,57,25,32,0,40,8,48,16,56,24};
		for(int i = 0; i < 64; i++) {
			d.add(x.get(IP_1[i]));
		}
	}

   //make arraylist of binary back to hex string
	public String converter2 (ArrayList<Integer> num) {
		String output = "";
		for(int i = 0; i < 16; i++) {
			int m = i *4;
			int n = num.get(m)*8+num.get(m+1)*4+num.get(m+2)*2+num.get(m+3);
			if(n>=10) {
				switch(n) {
				case(10):
					output = output.concat("A");
					break;
				case(11):
					output = output.concat("B");
					break;
				case(12):
					output = output.concat("C");
					break;
				case(13):
					output = output.concat("D");
					break;
				case(14):
					output = output.concat("E");
					break;
				case(15):
					output = output.concat("F");
					break;
				default:
					break;
				}
			}else {
				output = output.concat(String.valueOf(n));
			}
		}
		return output;
	}
}
