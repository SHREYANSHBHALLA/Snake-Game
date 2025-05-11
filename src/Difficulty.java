public enum Difficulty {
    EASY(150, 1.0, 3),
    MEDIUM(100, 1.5, 5),
    HARD(50, 2.0, 10);

    public final int gameSpeed;
    public final double scoreMultiplier;
    public final int growthRate;

    Difficulty(int speed, double multiplier, int growth) {
        this.gameSpeed = speed;
        this.scoreMultiplier = multiplier;
        this.growthRate = growth;
    }
}