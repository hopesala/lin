import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;

public class lab {
	public static class Service {
		private String name;
		private double numbet1;
		private double number2;
		private double number3;
		private double number4;

		public String getName() {
			return name;
		}

		public void setName(String name) {
			this.name = name;
		}

		public double getNumbet1() {
			return numbet1;
		}

		public void setNumbet1(double numbet1) {
			this.numbet1 = numbet1;
		}

		public double getNumber2() {
			return number2;
		}

		public void setNumber2(double number2) {
			this.number2 = number2;
		}

		public double getNumber3() {
			return number3;
		}

		public void setNumber3(double number3) {
			this.number3 = number3;
		}

		public double getNumber4() {
			return number4;
		}

		public void setNumber4(double number4) {
			this.number4 = number4;
		}
	}

	public static class Answers {
		private String name;
		private double r;
		private double c;

		public String getName() {
			return name;
		}

		public void setName(String name) {
			this.name = name;
		}

		public double getR() {
			return r;
		}

		public void setR(double r) {
			this.r = r;
		}

		public double getC() {
			return c;
		}

		public void setC(double c) {
			this.c = c;
		}

		public double getQ() {
			return r - c / 100.0;
		}
	}

	public static void main(String args[]) {

		// 读入数据
		String path1 = "PROCESS.txt";
		String path2 = "SERVICE.txt";
		String path3 = "REQ.txt";
		long startTime=System.currentTimeMillis(); //获取结束时间
	


		File f1 = new File(path1);
		File f2 = new File(path2);
		File f3 = new File(path3);

		String[] process = new String[10];
		Service[][] sermap = new Service[30][11];
		double[][] req = new double[10][2];

		try {

			// 读取process
			FileInputStream fis = new FileInputStream(f1);
			InputStreamReader reader = new InputStreamReader(fis);
			BufferedReader buffer = new BufferedReader(reader);

			String s;
			int index1 = 0;
			while ((s = buffer.readLine()) != null) {
				process[index1] = "";
				if (s.length() > 1) {
					for (int i = 0; i < s.length(); i++) {
						char ch = s.charAt(i);
						if (ch >= 'A' && ch <= 'Z') {
							if (!process[index1].contains(String.valueOf(ch))) {
								process[index1] += ch;
							}
						}
					}
				}
				index1++;
			}

			// 读取service
			fis = new FileInputStream(f2);
			reader = new InputStreamReader(fis);
			buffer = new BufferedReader(reader);

			while ((s = buffer.readLine()) != null) {
				String[] strings = s.split(" ");

				char activity = 0;
				Service ser = new Service();
				if (strings.length == 5) {
					String[] ss = strings[0].split("-");
					activity = ss[0].charAt(0);
					ser.setName(strings[0]);
					ser.setNumbet1(Double.valueOf(strings[1]));
					ser.setNumber2(Double.valueOf(strings[2]));
					ser.setNumber3(Double.valueOf(strings[3]));
					ser.setNumber4(Double.valueOf(strings[4]));
					int i = activity - 'A';
					int j = Integer.parseInt(new java.text.DecimalFormat("0")
							.format(ser.getNumber2() * 100.0)) - 90;
					// System.out.println(i + " " + j);
					if (sermap[i][j] == null) {
						sermap[i][j] = ser;
					} else {
						Service tmp = sermap[i][j];
						if (ser.getNumber4() < tmp.getNumber4())
							sermap[i][j] = ser;
					}
				}
			}


			// 读取req
			fis = new FileInputStream(f3);
			reader = new InputStreamReader(fis);
			buffer = new BufferedReader(reader);

			int index2 = 0;
			while ((s = buffer.readLine()) != null) {
				String[] line = s.split(",");
				int num_token = 1;
				for (String token : line) {
					if (token.length() < 1)
						continue;
					if (num_token % 2 == 1) {
						req[index2][0] = Double.valueOf(token.substring(1));
					} else {
						req[index2][1] = Double.valueOf(token.substring(0,
								token.length() - 1));
					}
					num_token++;
				}
				index2++;
			}

			buffer.close();
			reader.close();
			fis.close();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		// 查找最优解
		String outputstring = "";
		Answers[] answers = new Answers[4]; 
		for (int index = 0; index < 4; index++) {
			int maxn = Integer.parseInt(new java.text.DecimalFormat("0")
					.format(req[index][1] * 100.0)) + 1;
			Answers[][] ansmap = new Answers[26][maxn];

			for (int i = 0; i < process[index].length(); i++) {
				char ch = process[index].charAt(i);
				int num = ch - 'A';
				if (i == 0) {
					for (int j = 0; j < 11; j++) {
						Service ser = sermap[num][j];
						Answers answer = new Answers();
						answer.setName(ser.getName());
						answer.setC(ser.getNumber4());
						answer.setR(ser.getNumber2());

						int cc = Integer.parseInt(new java.text.DecimalFormat(
								"0").format(answer.getC() * 100.0));
						if (cc >= maxn)
							continue;
						if (ansmap[i][cc] == null) {
							ansmap[i][cc] = answer;
						} else {
							Answers answer1 = ansmap[i][cc];
							if (answer1.getR() < answer.getR()) {
								ansmap[i][cc] = answer;
							}
						}
					}
				} else {
					for (int k = 0; k < maxn; k++) {
						if (ansmap[i - 1][k] != null) {
							for (int j = 0; j < 11; j++) {
								Answers lastans = ansmap[i - 1][k];
								Service ser = sermap[num][j];
								Answers answer = new Answers();
								answer.setName(lastans.getName() + " "
										+ ser.getName());
								answer.setC(lastans.getC() + ser.getNumber4());
								answer.setR(lastans.getR() * ser.getNumber2());

								int cc = Integer
										.parseInt(new java.text.DecimalFormat(
												"0").format(answer.getC() * 100.0));
								if (cc >= maxn)
									continue;
								if (ansmap[i][cc] == null) {
									ansmap[i][cc] = answer;
								} else {
									Answers answer1 = ansmap[i][cc];
									if (answer1.getR() < answer.getR()) {
										ansmap[i][cc] = answer;
									}
								}
							}
						}
					}
				}
			}

			Answers ans = null;
			for (int i = 0; i < maxn; i++) {
				if (ansmap[process[index].length() - 1][i] != null) {
					if (ans == null)
						ans = ansmap[process[index].length() - 1][i];
					else {
						Answers t = ansmap[process[index].length() - 1][i];
						if (ans.getQ() < t.getQ())
							ans = t;
					}
				}
			}
			answers[index] = ans;
		}
		
		//输出格式
		String fin = "";
		try {
			FileInputStream fis = new FileInputStream(new File("PROCESS.txt"));
			InputStreamReader reader = new InputStreamReader(fis);
			BufferedReader buffer = new BufferedReader(reader);
			
			int i = 0;
			String s = "";
			while((s = buffer.readLine()) != null) {
				String[] ss = answers[i].getName().split(" ");
				for(int j = 0; j < s.length(); j++) {
					char ch = s.charAt(j);
					
					
					if(ch>='A' && ch <='Z') {
						String sss = "";
						for(int k = 0; k < ss.length; k++) {
							if(ss[k].contains(String.valueOf(ch))) {
								sss = ss[k];
							}
						}
						fin += sss;
					} else {
						fin += ch;
					}
				}
				
				fin += ",Reliability=" + String.valueOf(answers[i].getR());
				fin += ",Cost=" + String.valueOf(answers[i].getC());
				fin += ",Q=" + String.valueOf(answers[i].getQ());
				fin += "\r\n";
				i++;
			}
			
			
			buffer.close();
			reader.close();
			fis.close();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		System.out.println(fin);

		//输出到文件
		try {
			//FileWriter fw = new FileWriter(path, true);
			FileOutputStream fos = new FileOutputStream(new File("RESULT.txt"));
			fos.write(fin.getBytes());
			fos.flush();
			fos.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println("finished!");
		long endTime=System.currentTimeMillis(); //获取结束时间
		System.out.println("程序运行时间： "+(endTime-startTime)+"ms");

	}
}
