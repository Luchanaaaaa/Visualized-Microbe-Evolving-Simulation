public class ThreadDemo {
    public static void main(String[] args) throws Exception {
        Resource res = new Resource();
        SubThread st = new SubThread(res);
        AddThread at = new AddThread(res);
        new Thread(at, "加法-A ").start();
        new Thread(at, "加法-B ").start();
        new Thread(st, "减法-X ").start();
        new Thread(st, "减法-Y ").start();
    }
}
class AddThread implements Runnable {
    private Resource resource;
    public AddThread(Resource resource) {
        this.resource = resource;
    }
    @Override
    public void run() {
        for (int i = 0; i < 10; i++) {
            try {
                this.resource.add();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
class SubThread implements Runnable {
    private Resource resource;
    public SubThread(Resource resource) {
        this.resource = resource;
    }
    @Override
    public void run() {
        for (int i = 0; i < 10; i++) {
            try {
                this.resource.sub();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
class Resource {//定义一个操作的资源
    private int num = 0;//这个是要进行加减操作的数据
    private boolean flag = true;//加减的切换
    //flag = true：表示可以进行加法操作，但是无法进行减法操作
    //flag = false：表示可以进行减法操作，但是无法进行加法操作
    public synchronized void add() throws InterruptedException {//执行加法操作
        if (flag == false) {//现在需要执行的是减法操作，加法操作要等待
            //System.out.println("【加法操作 -" + Thread.currentThread().getName() + "】进行等待");
            super.wait();
           // System.out.println("【加法操作 -" + Thread.currentThread().getName() + "】被释放");
        }
        Thread.sleep(100);

            this.num++;
            System.out.println("【加法操作 -" + Thread.currentThread().getName() + "】num = " + this.num);
            this.flag = false;//加法操作执行完毕，需要执行减法操作
            super.notifyAll();//唤醒全部等待线程



    }
    public synchronized void sub() throws InterruptedException {//执行减法操作
        if (flag == true) { //减法操作需要等待，注意：一定要用while 详见：https://www.cnblogs.com/LeeScofiled/p/7225562.html
           // System.out.println("【减法操作 -" + Thread.currentThread().getName() + "】进行等待");
            System.out.println("【现在是 -" + Thread.currentThread().getName() + "】准备阻塞");
            super.wait();
           System.out.println("苏醒之后【现在是 -" + Thread.currentThread().getName() + "】在执行");
        }


        Thread.sleep(200);
            this.num--;
            System.out.println("【减法操作 -" + Thread.currentThread().getName() + "】num = " + this.num);
            this.flag = true;
            super.notifyAll();


    }
}
