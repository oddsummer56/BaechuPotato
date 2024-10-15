import dev.robocode.tankroyale.botapi.*;
import dev.robocode.tankroyale.botapi.events.*;

public class MyFirstBot extends Bot {

    int turnDirection = 1;
    // The main method starts our bot
    public static void main(String[] args) {
        new MyFirstBot().start();
    }

    // Constructor, which loads the bot config file
    MyFirstBot() {
        super(BotInfo.fromFile("MyFirstBot.json"));
    }

    // Called when a new round is started -> initialize and do some movement
    @Override
    public void run() {
        Color bluesky = Color.fromString("#82CAFF");
        setBodyColor(bluesky);
        setTurretColor(bluesky);
        setRadarColor(Color.YELLOW);
        setScanColor(Color.RED);
        setBulletColor(Color.CYAN);


        // Repeat while the bot is running
        while (isRunning()) {
            while (isRunning()) {
                turnLeft(10 * turnDirection);
            }

            if (getX() < 50 || getY() < 50 || getX() > getArenaWidth() - 50 || getY() > getArenaHeight() - 50) {
                // 벽에 가까워지면 회피
                turnLeft(90);
                forward(100);
            }

            else {
                forward(Math.random() * 200 + 50);
                turnGunRight(360);
                back(Math.random() * 200 + 50);
                turnGunRight(360);
            }
        }

    }

        // We scanned another bot -> go ram it
    @Override
    public void onScannedBot(ScannedBotEvent e) {
        turnToFaceTarget(e.getX(), e.getY());

        var distance = distanceTo(e.getX(), e.getY());
        forward(distance + 5);

        rescan(); // Might want to move forward again!
    }

    // We have hit another bot -> turn to face bot, fire hard, and ram it again!
    @Override
    public void onHitBot(HitBotEvent e) {
        turnToFaceTarget(e.getX(), e.getY());

        // Determine a shot that won't kill the bot...
        // We want to ram him instead for bonus points
        if (e.getEnergy() > 16) {
            fire(3);
        } else if (e.getEnergy() > 10) {
            fire(2);
        } else if (e.getEnergy() > 4) {
            fire(1);
        } else if (e.getEnergy() > 2) {
            fire(.5);
        } else if (e.getEnergy() > .4) {
            fire(.1);
        }

        forward(40); // Ram him again!
    }

    // Method that turns the bot to face the target at coordinate x,y, but also sets the
    // default turn direction used if no bot is being scanned within in the run() method.
    private void turnToFaceTarget(double x, double y) {
        setTargetSpeed(8);

        var bearing = bearingTo(x, y);
        if (bearing >= 0) {
            turnDirection = 1;
        } else {
            turnDirection = -1;
        }
        turnLeft(bearing);
    }
}

