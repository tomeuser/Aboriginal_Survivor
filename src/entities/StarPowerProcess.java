package entities;

import gamestates.Playing;

public class StarPowerProcess implements Runnable{
	
	private Playing playing;
    private volatile boolean isRunning = true;

	public StarPowerProcess(Playing playing) {
		this.playing = playing;
	}
	
    public void stop() {
        isRunning = false;
    }

	@Override
	public void run() {
		System.out.println("No Damage starts");
		playing.getEnemyManager().setEnemyDamage(0);
		playing.getPlayer().setStarPowerActive(true);
		try {
            Thread.sleep(10000); // Pause für 1 Sekunde
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
		playing.getStarManager().minInstances();
		System.out.println("Instance after reduce:" + playing.getStarManager().getInstances());
		if(playing.getStarManager().getInstances() <= 0) {
			playing.getEnemyManager().setEnemyDamage(25);
			playing.getPlayer().setStarPowerActive(false);
			System.out.println("No Damage stops");
		}
	}
}
