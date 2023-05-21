import java.util.ArrayList;
import java.util.Random;

public class RSA {
	public int getprime(int bit) {
		Random r = new Random();
		int k;
		do {
			k = r.nextInt((int)Math.pow(2, bit-1))+ (int)Math.pow(2, bit-1);
		}while(!isPrime(k));
		
		return k;	
	}
	
	public boolean isPrime(int k) {
		int i = 2;
		boolean m = false;
		while (i <= Math.sqrt(k)) {
			if (k % i == 0) {
				m = true;
				break;
			}
			i++;
		}
		
		if (m) {
			return false;
		}
		return true;
	}
	
	
	public boolean coprime(int m, int n) {
		int k;
		do {
			k = m % n;
			m = n;
			n = k;
		}while(k!=0 && k!=1);
		
		if (k==0) {
			return false;
		}
		return true;
	}
	
	
	public int[] getKey() {
		boolean done = true;
		int[] key = new int[3];
		while(done) {
			done = false;
			int m = getprime(8);
			int n;
			do {
				n = getprime(8);
			}while(m==n);
			
			key[0] = m*n;
			
			
			int k = (m-1)*(n-1);
			
			int e;
			Random r = new Random();
			do {
				e = r.nextInt(k-2)+2;
			}while(!coprime(e,k));
			
			key[1] = e;
			int p = 1;
			
			while ((1+p*k)%e != 0) {
				p++;
				if((1+p*(long)k)/(long)e > (long)Integer.MAX_VALUE) {
					done = true;
					break;
				}
			}
			int d = (int)((1+p*(long)k)/(long)e);
			key[2] = d;
		}
		
		return key;
	}
	
	
	public String encryption(int ip, int key, int n) {
		ArrayList<Integer> op = new ArrayList<Integer>();
		long k = (long)ip;
		for(int j = 0; j < n-1;j++) {
			k = (((long)k * (long)ip) % (long)key);
		}
		op.add((int)k);
		String m = "";
		for(int i = 0; i < op.size(); i++) {
			for(int j = 0; j < 5-String.valueOf(op.get(i)).length(); j++) {
				m = m.concat(new String("0"));
			}
			m = m.concat(String.valueOf(op.get(i)));
		}
		
		return m;
	}
	
	public ArrayList<Integer> decryption(String ip, int key, int d){
		ArrayList<Integer> op = new ArrayList<Integer>();
		ArrayList<Integer> m = new ArrayList<Integer>();
		
		int k;
		
		for(int i = 0; i < ip.length()/5; i++) {
			k = Integer.parseInt(ip.substring(i*5, (i+1)*5));
			m.add(k);
		}
		
		long n;
		
		for(int i = 0; i < m.size(); i++) {
			n = (long)m.get(i);
			for(int j = 0; j < d-1;j++) {
				n = (n * (long)m.get(i)) % (long)key;
			}
			op.add((int)n);
		}
		return op;
		
	}
	
}
