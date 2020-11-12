package it.univaq.disim.mwt.j2etpapp.utils;

import it.univaq.disim.mwt.j2etpapp.business.ChannelBO;
import it.univaq.disim.mwt.j2etpapp.business.RoleBO;
import it.univaq.disim.mwt.j2etpapp.business.ServiceBO;
import it.univaq.disim.mwt.j2etpapp.business.UserChannelRoleBO;
import it.univaq.disim.mwt.j2etpapp.domain.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class PermissionChecker {

    @Autowired
    private UserChannelRoleBO userChannelRoleBO;
    @Autowired
    private ChannelBO channelBO;
    @Autowired
    private ServiceBO serviceBO;
    @Autowired
    private RoleBO roleBO;

    public boolean hasPermissionOnChannel(UserClass currentUser, ChannelClass channel, String permission){
        if(currentUser == null || channel == null || permission == null) {
            return false;
        }

        UserChannelRole userChannelRole = userChannelRoleBO.findByChannelIdAndUserId(channel.getId(), currentUser.getId());
        ServiceClass joinChannel = serviceBO.findByName("join_channel");

        if(userChannelRole != null) {
            if(joinChannel.getName().equals(permission)) {
                return false;
            }
            RoleClass role = userChannelRole.getRole();
            for (ServiceClass service : role.getServices()) {
                if(service.getName().equals(permission)) {
                    return true;
                }
            }
        } else {
            if(joinChannel.getName().equals(permission) && !(channelBO.getSoftBannedUsers(channel.getId()).contains(currentUser))) {
                return true;
            }
        }
        return false;
    }

    public boolean hasPermissionOnPost(UserClass currentUser, PostClass post, String permission){
        if(currentUser == null || post == null || permission == null) {
            return false;
        }

        UserChannelRole userChannelRole = userChannelRoleBO.findByChannelIdAndUserId(post.getChannelId(), currentUser.getId());
        RoleClass creator = roleBO.findByName("creator");
        RoleClass admin = roleBO.findByName("admin");
        RoleClass moderator = roleBO.findByName("moderator");

        if(userChannelRole != null) {
            RoleClass role = userChannelRole.getRole();
            for (ServiceClass service : role.getServices()) {
                if(service.getName().equals(permission) && (post.getUserId().equals(currentUser.getId()) || creator.equals(role) || admin.equals(role) || moderator.equals(role))) {
                    return true;
                }
            }
        }
        return false;
    }

    public boolean hasPermissionOnReply(UserClass currentUser, ReplyClass reply, String permission){
        if(currentUser == null || reply == null || permission == null) {
            return false;
        }

        UserChannelRole userChannelRole = userChannelRoleBO.findByChannelIdAndUserId(reply.getChannelId(), currentUser.getId());
        RoleClass creator = roleBO.findByName("creator");
        RoleClass admin = roleBO.findByName("admin");

        if(userChannelRole != null) {
            RoleClass role = userChannelRole.getRole();
            for (ServiceClass service : role.getServices()) {
                if(service.getName().equals(permission) && (reply.getUserId().equals(currentUser.getId())) || creator.equals(role) || admin.equals(role)) {
                    return true;
                }
            }
        }
        return false;
    }

    public boolean hasPermissionOnUser(UserClass currentUser, UserClass user, String permission){
        if(currentUser == null || user == null || permission == null) {
            return false;
        }

        for (ServiceClass service : currentUser.getGroup().getServices()) {
            if(service.getName().equals(permission) && currentUser.getId().equals(user.getId())) {
                return true;
            }
        }
        return false;
    }
}
