package old;

public class RandomNum {

	private Integer num;
	private Integer diff;
	private Integer max;
	
	public RandomNum(String s) {
		this.num = Integer.valueOf(s);
		this.max = 1000;
		this.diff = this.max - this.num;
	}

	public RandomNum(String s, int max) {
		this.num = Integer.valueOf(s);
		this.max = max;
		this.diff = this.max - this.num;
	}

	public RandomNum(int num, int max) {
		this.num = num;
		this.max = max;
		this.diff = this.max - this.num;
	}
	
	public Integer getNum() {
		return num;
	}

	public void setNum(Integer num) {
		this.num = num;
	}

	public Integer getMax() {
		return max;
	}

	public void setMax(Integer max) {
		this.max = max;
	}

	public Integer getDiff() {
		return diff;
	}

	public void setDiff(Integer diff) {
		this.diff = diff;
	}
		
}
