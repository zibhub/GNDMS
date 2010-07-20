
module GNDMS

  def dmsLayout(prj, out)
    prjOut = prj + '/' + out
    l = Layout.new
    l[:source, :main, :java] = _(prj + '/src')
    l[:source, :main, :groovy] = _(prj + '/groovy')
    l[:source, :main, :resources] = _(prj + '/resources')
    l[:target, :main, :classes] = _(prjOut + '/production')
    l[:target, :main, :resources] = _(prjOut + '/production-resources')
    l[:target] = _('lib/' + prj)
    return l
  end

  def testEnv(var, descr)
    if (ENV[var] == nil)
        puts 'Error: $' + var + ' must be set to ' + descr
        exit 1
    end
  end

  def testTool(tool)
    if (! system("which " + tool + '>/dev/null'))
      puts 'Error: `' + tool + '\' does not appear to be in your $PATH (tested using `which\')'
      exit 2
    end
  end

end
