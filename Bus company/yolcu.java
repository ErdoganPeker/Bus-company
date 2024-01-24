package ex_4_advnced_vr;
public  class yolcu {
	String yolcu_ismi;
	String yolcu_cinsiyeti;
	int yolcu_yasi;
	String yolcu_tc;
	int koltuk_no;
	yolcu(String yolcu_ismi,String yolcu_cinsiyeti,int yolcu_yasi,String yolcu_tc,int koltuk_no){
		this.yolcu_ismi = yolcu_ismi;
		this.yolcu_cinsiyeti = yolcu_cinsiyeti;
		this.yolcu_yasi = yolcu_yasi;
		this.yolcu_tc = yolcu_tc;
		this.koltuk_no = koltuk_no;
		
		
	}
	public String Get_name() {
		return yolcu_ismi;
	}
	public int Get_age() {
		return yolcu_yasi;
	}
	public String Get_seatnum() {
		return yolcu_cinsiyeti;
	}
	public String Get_gender() {
		return yolcu_cinsiyeti;
	}
	
	

	public void Set_name(String yolcu_ismi) {
		this.yolcu_ismi = yolcu_ismi;
	}
	public void Set_age(int yolcu_yasi) {
		this.yolcu_yasi = yolcu_yasi;
	}
	public void Set_seatnum(String yolcu_tc) {
		this.yolcu_tc = yolcu_tc;
	}
	public void Set_gender(String yolcu_cinsiyeti) {
		this.yolcu_cinsiyeti = yolcu_cinsiyeti;
	}
	

}
