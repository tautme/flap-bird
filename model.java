//import java.util.Random;
import java.util.LinkedList;
import java.util.Iterator;
import java.lang.Math;

class Model
{
	//int count;
	LinkedList<Sprite> list;
	Random r;
	boolean over;
	static final int do_nothing = 1;
	static final int flap = 2;
	static final int throw_pie = 3;
	static final int flap_and_throw = 4;

	Model() {
		over = false;
		r = new Random(10111);
		this.list = new LinkedList<Sprite>();
		Bird b = new Bird(r,list);
		list.add(b);
		//count = 0;
		int first = 400;
		int diff = 250;
		Tube t1 = new Tube(r,list,first);
		Tube t2 = new Tube(r,list,first + diff);
		Tube t3 = new Tube(r,list,first + 2*diff);
		
		Sprite s = null;
		
		s = (Sprite)t1;
		list.add(s);
		s = (Sprite)t2;
		list.add(s);
		s = (Sprite)t3;
		list.add(s);
	}
	
	Model(Model m){
		//this.count = m.count;
		this.over = m.over;
		r = new Random(m.r);
		list = new LinkedList<Sprite>();
		Iterator<Sprite> it = m.list.iterator();
		while(it.hasNext()){
			Sprite s = it.next();
			Sprite copy = s.replicate();
			list.add(copy);
			copy.list = this.list;
		}
		
	}

	public void update() {
		boolean addTube = false;
		int maxHor = 0;
		Iterator<Sprite> it = list.iterator();
		while (it.hasNext()){
			Sprite sprite = null;
			sprite = it.next();
			if(sprite.update()){
				if(sprite.getType() == 1){
					over = true;
				}
				else {
					if(sprite.getType() == 2){
						addTube = true;
					}
					it.remove();
				}
			}else if(sprite.getType() == 2){
				if(sprite.x > maxHor) maxHor = sprite.x;
			}
			
		}
		
		if(addTube){
			Tube t = new Tube(r,list,maxHor + 250);
			Sprite sprite = (Sprite)t;
			list.add(sprite);
		}
		/* Sprite sprite = null;
		if(count == 5){
			Tube t = new Tube(r,list);
			sprite = (Sprite)t;
			list.add(sprite);
		}else if(count == 70){
			Tube t = new Tube(r,list);
			sprite = (Sprite)t;
			list.add(sprite);
		}else if(count == 135){
			Tube t = new Tube(r,list);
			sprite = (Sprite)t;
			list.add(sprite);
		} */
		
		//count++;
	}
	


	
	public void onLeftClick() {
		Iterator<Sprite> it = list.descendingIterator();
		while (it.hasNext())
		{
			Sprite sprite;
			sprite = it.next();
			if (sprite.getType() == 1)
			{
				Bird b = (Bird)sprite;
				b.flap();
				break;
			}
		}
	}
	
	public void onRightClick() {
		if(!over){
			Pie p = new Pie(r, list);
			list.add(p);
		}
		
	}
	
	public int evaluateAction(int action, int depth){
		if(depth == 5){
			int[] a = {1000,1000,1000,1000,1000};
			boolean[] orient = new boolean[5];
			int i = 0;
			Bird b = null;
			Tube t = null;
			Iterator<Sprite> it = list.iterator();
			while(it.hasNext()){
				Sprite s = it.next();
				if(s.getType() == 1){
					b = (Bird)s;
				}else if(s.getType() == 2){
					t = (Tube)s;
					a[i] = t.x;
					orient[i] = t.isUp;
					i++;
				}
			}
			if(b.getCollision()){
				return 0;
			}
			else{
				int tube = 0;
				for(int j = 0; j < 5; j++){
					if(Math.abs(b.x - a[j]) < Math.abs(b.x - a[tube])){
						tube = j;
					}
				}
				if(Math.abs(b.x - a[tube]) < 100){
					if(orient[tube]) return 500 - Math.abs(b.y - 110);
					else return 500 - Math.abs(b.y - 210);
				}
				return 500 - Math.abs(b.y - 160);
			}
		}
		else{
			Model m = new Model(this);
			switch(action){
				case 1: break;
				case 2: m.onLeftClick();
				case 3: m.onRightClick();
				case 4: m.onLeftClick();
						m.onRightClick();
			}
			m.update();
			if(depth % 15 == 0){
				depth++;
				int v1 = 0;
				int v2 = 0;
				int v3 = 0;
				int v4 = 0;
				v1 = m.evaluateAction(1,depth);
				v2 = m.evaluateAction(2,depth);
				v3 = m.evaluateAction(3,depth);
				v4 = m.evaluateAction(4,depth);
				//int value = 0;
				return Math.max(v4,Math.max(v3,Math.max(v2,v1)));
				//return value;
				//return Math.max(m.evaluateAction(4,depth),Math.max(Math.max(m.evaluateAction(3,depth),m.evaluateAction(2,depth)),m.evaluateAction(1,depth)));
			}else{
				depth++;
				return m.evaluateAction(1,depth);
			}
		}
	}
	
	/* public void printBird(){
		Sprite s = null;
		Bird b = null;
		Iterator<Sprite> it = list.iterator();
		while(it.hasNext()){
			s = it.next();
			if(s.getType() == 1){
				b = (Bird)s;
				break;
			}
		}
		
		System.out.print(Math.abs(b.y - 250));
		if(b.collision == true)
			System.out.println("y");
		else System.out.println("n");
	} */
}







