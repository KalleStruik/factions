package io.icker.factions.command;

import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;

import io.icker.factions.database.Faction;
import io.icker.factions.database.Member;
import io.icker.factions.util.Message;

import com.mojang.brigadier.Command;
import net.minecraft.server.PlayerManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayerEntity;

public class DisbandCommand implements Command<ServerCommandSource> {
	@Override
	public int run(CommandContext<ServerCommandSource> context) throws CommandSyntaxException {
		ServerCommandSource source = context.getSource();
		ServerPlayerEntity player = source.getPlayer();

		Member member = Member.get(player.getUuid());
		Faction faction = member.getFaction();

		new Message(player.getName().asString() + " disbanded the faction").send(faction);
		faction.remove();

		PlayerManager manager = source.getServer().getPlayerManager();
		for (ServerPlayerEntity p : manager.getPlayerList()) {
			manager.sendCommandTree(p);
		}
		return 1;
	}
}