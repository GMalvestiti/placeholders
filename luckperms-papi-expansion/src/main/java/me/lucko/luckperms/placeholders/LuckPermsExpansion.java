/*
 * This file is part of LuckPerms, licensed under the MIT License.
 *
 *  Copyright (c) lucko (Luck) <luck@lucko.me>
 *  Copyright (c) contributors
 *
 *  Permission is hereby granted, free of charge, to any person obtaining a copy
 *  of this software and associated documentation files (the "Software"), to deal
 *  in the Software without restriction, including without limitation the rights
 *  to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 *  copies of the Software, and to permit persons to whom the Software is
 *  furnished to do so, subject to the following conditions:
 *
 *  The above copyright notice and this permission notice shall be included in all
 *  copies or substantial portions of the Software.
 *
 *  THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 *  IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 *  FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 *  AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 *  LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 *  OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 *  SOFTWARE.
 */

package me.lucko.luckperms.placeholders;

import me.clip.placeholderapi.PlaceholderAPIPlugin;
import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import me.clip.placeholderapi.util.TimeUtil;
import me.lucko.luckperms.api.LuckPermsApi;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

/**
 * PlaceholderAPI Expansion for LuckPerms, implemented using the LuckPerms API.
 */
public class LuckPermsExpansion extends PlaceholderExpansion {
    private static final String IDENTIFIER = "luckperms";
    private static final String PLUGIN_NAME = "LuckPerms";
    private static final String AUTHOR = "Luck";
    private static final String VERSION = "4.1";

    private LPPlaceholderProvider provider;

    @Override
    public boolean canRegister() {
        return Bukkit.getServicesManager().isProvidedFor(LuckPermsApi.class);
    }

    @Override
    public boolean register() {
        if (!canRegister()) {
            return false;
        }

        LuckPermsApi api = Bukkit.getServicesManager().getRegistration(LuckPermsApi.class).getProvider();
        this.provider = new LPPlaceholderProvider(api) {
            @Override
            protected String formatTime(int time) {
                return TimeUtil.getTime(time);
            }

            @Override
            protected String formatBoolean(boolean b) {
                return b ? PlaceholderAPIPlugin.booleanTrue() : PlaceholderAPIPlugin.booleanFalse();
            }
        };
        return super.register();
    }

    @Override
    public String onPlaceholderRequest(Player player, String identifier) {
        if (player == null || this.provider == null) {
            return "";
        }

        return this.provider.handle(player, identifier);
    }

    @Override
    public String getIdentifier() {
        return IDENTIFIER;
    }

    @Override
    public String getPlugin() {
        return PLUGIN_NAME;
    }

    @Override
    public String getAuthor() {
        return AUTHOR;
    }

    @Override
    public String getVersion() {
        return VERSION;
    }

}
