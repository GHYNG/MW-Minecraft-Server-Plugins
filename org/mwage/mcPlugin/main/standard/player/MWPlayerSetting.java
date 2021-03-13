package org.mwage.mcPlugin.main.standard.player;
import java.util.UUID;
import org.bukkit.entity.Player;
/**
 * ���ڵ������������Ϣ���ࡣ
 * <p>
 * ע�⣺������д�����趨��Ϣ����ʱ�ģ�
 * �����ŷ�������������reload��ʧ��
 * �����Ҫ���ڵĴ�����Ϣ��
 * Ӧ��Ѱ�������ķ�����
 * <p>
 * ����һ�������࣬��ζ���Ӳ�������ô洢��Ӧ�ü̳д��ࡣ
 * 
 * @author GHYNG
 */
public abstract class MWPlayerSetting {
	/**
	 * ��ҵ�UUID��
	 */
	public final UUID uuid;
	/**
	 * ����ָ����UUID������һ���µĶ���
	 * 
	 * @param uuid
	 *            ָ����UUID��
	 */
	public MWPlayerSetting(UUID uuid) {
		this.uuid = uuid;
	}
	/**
	 * ����ָ����Player���󣬴���һ���µĶ���
	 * 
	 * @param player
	 *            ָ����Player����
	 */
	public MWPlayerSetting(Player player) {
		this(player.getUniqueId());
	}
}