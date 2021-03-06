package it.univaq.disim.mwt.postd.presentation;

import it.univaq.disim.mwt.postd.business.*;
import it.univaq.disim.mwt.postd.domain.*;
import it.univaq.disim.mwt.postd.helpers.TemplateHelper;
import it.univaq.disim.mwt.postd.utils.UtilsClass;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("discover/channel")
public class DiscoverChannelController {

    @Autowired
    private ChannelBO channelBO;
    @Autowired
    private PostBO postBO;
    @Autowired
    private UserChannelRoleBO userChannelRoleBO;
    @Autowired
    private TagBO tagBO;

    @Autowired
    private UtilsClass utilsClass;

    @Autowired
    private TemplateHelper templateHelper;

    @GetMapping("{id}")
    public String discoverChannel(@PathVariable("id") Long id, Model model) throws BusinessException {
        ChannelClass channel = channelBO.findById(id);
        Page<PostClass> postPage = postBO.findByChannelIdOrderByCreatedAtDescPaginated(id, 0, 10);
        model.addAttribute("post", new PostClass());
        model.addAttribute("channel", channel);
        model.addAttribute("postPage", postPage);
        model.addAttribute("templateHelper", templateHelper);
        UserClass principal = utilsClass.getPrincipal();
        model.addAttribute("principal", principal);
        UserChannelRole subscription = utilsClass.getSubscription(channel, principal);
        model.addAttribute("subscription", subscription);
        model.addAttribute("tags", tagBO.findAll());
        return "pages/discover/channel";
    }

    @GetMapping("{id}/posts/page/{pageId}")
    public String discoverChannelPostsPage(@PathVariable("id") Long id, @PathVariable("pageId") int pageId, Model model) throws BusinessException {
        ChannelClass channel = channelBO.findById(id);
        Page<PostClass> postPage = postBO.findByChannelIdOrderByCreatedAtDescPaginated(id, pageId, 10);
        model.addAttribute("channel", channel);
        model.addAttribute("postPage", postPage);
        model.addAttribute("templateHelper", templateHelper);
        UserClass principal = utilsClass.getPrincipal();
        model.addAttribute("principal", principal);
        UserChannelRole subscription = utilsClass.getSubscription(channel, principal);
        model.addAttribute("subscription", subscription);
        model.addAttribute("tags", tagBO.findAll());
        return "pages/discover/channel";
    }

    @GetMapping("{id}/posts/reported")
    @PreAuthorize("hasPermission(#id, 'it.univaq.disim.mwt.postd.domain.ChannelClass', 'global_unreport_post_in_channel')")
    public String discoverChannelPostsReported(@PathVariable("id") Long id, Model model) throws BusinessException {
        ChannelClass channel = channelBO.findById(id);
        Page<PostClass> postPage = postBO.findByChannelIdReportedOrderByCreatedAtDescPaginated(id, 0, 10);
        model.addAttribute("channel", channel);
        model.addAttribute("postPage", postPage);
        model.addAttribute("templateHelper", templateHelper);
        return "pages/discover/reported_posts";
    }

    @GetMapping("{id}/posts/reported/page/{pageId}")
    @PreAuthorize("hasPermission(#id, 'it.univaq.disim.mwt.postd.domain.ChannelClass', 'global_unreport_post_in_channel')")
    public String discoverChannelPostsReportedPage(@PathVariable("id") Long id, @PathVariable("pageId") int pageId, Model model) throws BusinessException {
        ChannelClass channel = channelBO.findById(id);
        Page<PostClass> postPage = postBO.findByChannelIdReportedOrderByCreatedAtDescPaginated(id, pageId, 10);
        model.addAttribute("channel", channel);
        model.addAttribute("postPage", postPage);
        model.addAttribute("templateHelper", templateHelper);
        return "pages/discover/reported_posts";
    }

    @GetMapping("{id}/members")
    @PreAuthorize("hasPermission(#id, 'it.univaq.disim.mwt.postd.domain.ChannelClass', 'view_channel_members')")
    public String discoverChannelMembers(@PathVariable("id") Long id, Model model) throws BusinessException {
        UserClass principal = utilsClass.getPrincipal();

        ChannelClass channel = channelBO.findById(id);
        Page<UserChannelRole> members = userChannelRoleBO.findByChannelIdPaginated(channel.getId(), 0, 10);
        model.addAttribute("channel", channel);
        model.addAttribute("members", members);
        model.addAttribute("templateHelper", templateHelper);
        model.addAttribute("principal", principal);
        model.addAttribute("userRole", getUserRole(principal, channel.getId()));
        return "pages/discover/members";
    }

    @GetMapping("{id}/members/page/{pageId}")
    @PreAuthorize("hasPermission(#id, 'it.univaq.disim.mwt.postd.domain.ChannelClass', 'view_channel_members')")
    public String discoverChannelMembersPage(@PathVariable("id") Long id, @PathVariable("pageId") int pageId, Model model) throws BusinessException {
        UserClass principal = utilsClass.getPrincipal();

        ChannelClass channel = channelBO.findById(id);
        Page<UserChannelRole> members = userChannelRoleBO.findByChannelIdPaginated(channel.getId(), pageId, 10);
        model.addAttribute("channel", channel);
        model.addAttribute("members", members);
        model.addAttribute("templateHelper", templateHelper);
        model.addAttribute("principal", principal);
        model.addAttribute("userRole", getUserRole(principal, channel.getId()));
        return "pages/discover/members";
    }

    @GetMapping("{id}/members/banned")
    @PreAuthorize("hasPermission(#id, 'it.univaq.disim.mwt.postd.domain.ChannelClass', 'softban_user_in_channel')")
    public String discoverChannelMembersBanned(@PathVariable("id") Long id, Model model) throws BusinessException {
        ChannelClass channel = channelBO.findById(id);
        model.addAttribute("channel", channel);
        return "pages/discover/banned_users";
    }


    private RoleClass getUserRole(UserClass principal, Long channelId) {
        return "logged".equals(principal.getGroup().getName()) ? userChannelRoleBO.findByChannelIdAndUserId(channelId, principal.getId()).getRole() : new RoleClass("administrator");
    }
}
