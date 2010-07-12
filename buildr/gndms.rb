
module GNDMS

  def dmsLayout(prj, out)
    prjOut = prj + '/' + out
    l = Layout.new
    l[:source, :main, :java] = _(prj + '/src')
    l[:source, :main, :groovy] = _(prj + '/groovy')
    l[:source, :main, :resources] = _(prj + '/resources')
    l[:target, :main, :classes] = _(prjOut + '/production')
    l[:target, :main, :resources] = _(prjOut + '/production-resources')
    l[:target] = _('extra/lib/' + prj)
    return l
  end

end
