
env = Environment(tools=('javac',))

src = Dir('src')
build = Dir('build')
classes = build.Dir('classes')

Java(classes, src)
Jar(target='jlox.jar', source=classes)
