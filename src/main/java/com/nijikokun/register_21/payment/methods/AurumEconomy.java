package com.nijikokun.register_21.payment.methods;

import com.projectposeidon.johnymuffin.UUIDManager;
import org.bukkit.plugin.Plugin;
import org.whoisjeb.aurum.Aurum;
import org.whoisjeb.aurum.data.AurumUser;
import java.util.UUID;
import com.nijikokun.register_21.payment.Method;

public class AurumEconomy implements Method {
    private Aurum aurum;

    public Object getPlugin() {
        return this.aurum;
    }

    public String getName() {
        return "Aurum";
    }

    public String getVersion() {
        return aurum.getDescription().getVersion();
    }

    public String format(double amount) {
        return "$" + String.format("%,.2f", amount);
    }

    public boolean hasBanks() {
        return false;
    }

    public boolean hasBank(String bank) {
        return false;
    }

    public boolean hasAccount(String name) {
        return Aurum.api().user(UUIDManager.getInstance().getUUIDFromUsername(name)).hasProperty("economy.balance");
    }

    public boolean hasBankAccount(String bank, String name) {
        return false;
    }

    public MethodAccount getAccount(String name) {
        UUID uuid = UUIDManager.getInstance().getUUIDFromUsername(name);
        return new AurumPersonalAccount(uuid);
    }

    public MethodBankAccount getBankAccount(String bank, String name) {
        return null;
    }

    public boolean isCompatible(Plugin plugin) {
        return true;
    }

    public void setPlugin(Plugin plugin) {
        this.aurum = (Aurum) plugin;
    }

    private static class AurumPersonalAccount implements MethodAccount {
        private UUID uuid;
        private AurumUser user;
        private double balance;

        private AurumPersonalAccount(UUID uuid) {
            this.uuid = uuid;
            this.user = Aurum.api().user(uuid);
            this.balance = user.getDouble("economy.balance");
        }

        public double balance() {
            return balance;
        }

        public boolean set(double amount) {
            return user.setBalance(amount);
        }

        public boolean add(double amount) {
            return user.addBalance(amount);
        }

        public boolean subtract(double amount) {
            return user.subtractBalance(amount, false);
        }

        public boolean multiply(double amount) {
            user.setProperty("economy.balance", (balance * amount));
            return true;
        }

        public boolean divide(double amount) {
            user.setProperty("economy.balance", (balance / amount));
            return true;
        }

        public boolean hasEnough(double amount) {
            return balance >= amount;
        }

        public boolean hasOver(double amount) {
            return balance > amount;
        }

        public boolean hasUnder(double amount) {
            return balance < amount;
        }

        public boolean isNegative() {
            return balance < 0;
        }

        public boolean remove() {
            return false;
        }
    }
}
