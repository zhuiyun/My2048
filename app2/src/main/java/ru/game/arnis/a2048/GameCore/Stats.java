package ru.game.arnis.a2048.GameCore;


import java.util.Locale;

public class Stats {

    public void setAmount(float amount) {
        this.amount = amount;
    }

    private float amount=0;


    public float getAmount() {
        return amount;
    }


    public String getStrAmount() {
        return String.format(Locale.ENGLISH,"%.1f",amount);
    }



    public void addAmountBasedOnLvl(int lvl,boolean allIterations){
        switch (lvl){
            case 1: if (allIterations) this.amount+=0.1f;break;
            case 2: if (allIterations)this.amount+=0.5f; else this.amount+=0.3f;break;
            case 3: if (allIterations) this.amount+=1f;break;
            case 4: if (allIterations) this.amount+=2f;break;
            case 5: if (allIterations)this.amount+=5f; else this.amount+=1f;break;
            case 6: if (allIterations) this.amount+=10f;break;
            case 7: if (allIterations)this.amount+=50f; else this.amount+=30f;break;
            case 8: if (allIterations) this.amount+=100f;break;
            case 9: if (allIterations)this.amount+=500f; else this.amount+=300f;break;
            case 10: if (allIterations) this.amount+=1000f;break;
            case 11: if (allIterations)this.amount+=5000f; else this.amount+=3000f;break;
            case 12: this.amount+=1f;break;
            case 13: if (allIterations)this.amount+=2f;
            case 14: if (allIterations) this.amount+=5f;else this.amount+=1f;break;
            case 15: if (allIterations) this.amount+=10f;break;
            case 16: if (allIterations)this.amount+=20f;
            case 17: if (allIterations) this.amount+=50f; else this.amount+=10f;break;
            case 18: if (allIterations) this.amount+=100f;break;
            default: break;


        }
    }

    public void clear() {
        amount=0;
    }


}
